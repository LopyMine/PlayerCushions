package net.lopymine.pc.skin.provider;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.*;
import lombok.*;
import net.lopymine.pc.api.Response;
import net.lopymine.pc.client.PlayerCushionsClient;
import net.lopymine.pc.data.*;
import net.lopymine.pc.skin.data.ParsedSkinData;
import net.lopymine.pc.thread.PlayerCushionsTaskExecutor;
import net.lopymine.pc.utils.texture.*;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.*;

@Setter
@Getter
public abstract class StandardSkinProvider implements SkinProvider {

	private final Map<String, CompletableFuture<Void>> reloadingFutures = new ConcurrentHashMap<>();
	private final Map<String, PlayerSkinData> cache = new ConcurrentHashMap<>();

	private boolean maxRequestsCheckEnabled;
	private int requestsCount = 0;
	private long lastRequestTime = 0L;

	protected StandardSkinProvider(boolean maxRequestsCheckEnabled) {
		this.maxRequestsCheckEnabled = maxRequestsCheckEnabled;
	}

	@Override
	@Nullable
	public PlayerSkinData getOrLoadData(String value) {
		if (!this.canProcess(value)) {
			return null;
		}

		PlayerSkinData data = this.getDataOrCreate(value);
		if (data.canStartDownloading()) {
			this.loadData(value, this.maxRequestsCheckEnabled, data);
		}

		return data;
	}


	public CompletableFuture<Void> loadData(String value, boolean checkMaxRequests, PlayerSkinData data) {
		if (checkMaxRequests) {
			// Max 10 requests per second
			long now = System.currentTimeMillis();
			if (now - this.lastRequestTime > 1000) {
				this.requestsCount   = 0;
				this.lastRequestTime = now;
			}
			if (this.requestsCount >= 10) {
				return CompletableFuture.completedFuture(null);
			}
			this.requestsCount++;
		}

		data.setState(LoadingState.WAITING_DOWNLOADING);

		return PlayerCushionsTaskExecutor.execute(() -> {
			int waitTime = 0;

			while (true) {
				data.setState(LoadingState.DOWNLOADING);

				Response<ParsedSkinData> response = this.loadDataFromAPI(value);
				if (response.value() == null) {
					LoadingState state = switch (response.statusCode()) {
						case 404 -> LoadingState.NOT_FOUND; // Not Found
						case 429 -> LoadingState.ERROR; // Too many requests
						default -> LoadingState.CRITICAL_ERROR;
					};

					if (state == LoadingState.ERROR) { // Too many requests, we can retry
						try {
							waitTime += 1000;
							Thread.sleep(waitTime);
							continue;
						} catch (Exception e) {
							data.setState(LoadingState.CRITICAL_ERROR);
							return;
						}
					}

					data.setState(state);
					return;
				}

				data.setState(LoadingState.REGISTERING);

				ParsedSkinData parsedSkinData = response.value();
				if (parsedSkinData.getSkinUrl() == null) {
					data.setState(LoadingState.CRITICAL_ERROR);
					return;
				}

				Identifier skinId = this.getSkinId(value);

				FailedAction onFailed = (throwable) -> {
					data.setState(LoadingState.CRITICAL_ERROR);
					PlayerCushionsClient.LOGGER.warn("Failed to download skin:", throwable);
				};

				SuccessAction onSuccess = (sprite) -> {
					data.setSkinId(sprite);
					data.setState(LoadingState.DOWNLOADED);
				};

				PlayerSkinUtils.downloadSkin(parsedSkinData.getSkinUrl(), skinId, onSuccess, onFailed, true);
				break;
			}
		});
	}

	private PlayerSkinData getDataOrCreate(String value) {
		return Optional.ofNullable(this.getFromCache(value))
				.orElseGet(() -> {
					PlayerSkinData data = this.createNewData(value);
					this.putToCache(value, data);
					return data;
				});
	}

	@Override
	public Set<String> getLoadedKeys() {
		return this.cache.keySet();
	}

	@Override
	public Collection<PlayerSkinData> getLoadedData() {
		return this.cache.values();
	}

	@Override
	public CompletableFuture<Void> reloadAll() {
		Set<CompletableFuture<?>> list = new HashSet<>();

		for (Entry<String, PlayerSkinData> entry : this.cache.entrySet()) {
			String key = entry.getKey();
			PlayerSkinData value = entry.getValue();

			CompletableFuture<Void> future = this.reloadingFutures.get(key);
			if (future != null) {
				list.add(future);
				continue;
			}

			list.add(this.reloadDataAndRegisterFuture(key, value));
		}

		return CompletableFuture.allOf(list.toArray(new CompletableFuture[0]));
	}

	@Override
	public CompletableFuture<Void> reloadOne(String value) {
		CompletableFuture<Void> future = this.reloadingFutures.get(value);
		if (future != null) {
			return future;
		}

		PlayerSkinData data = this.getFromCache(value);
		if (data == null) {
			return CompletableFuture.completedFuture(null);
		}

		return this.reloadDataAndRegisterFuture(value, data);
	}

	private CompletableFuture<Void> reloadDataAndRegisterFuture(String value, PlayerSkinData data) {
		data.destroy();

		CompletableFuture<Void> future = this.loadData(value, false, data)
				.whenComplete((r, e) -> {
					this.reloadingFutures.remove(value);
					if (e != null) {
						PlayerCushionsClient.LOGGER.error("Failed to reload data for \"{}\": ", value, e);
					}
				});
		this.reloadingFutures.put(value, future);
		return future;
	}

	protected abstract Response<ParsedSkinData> loadDataFromAPI(String value);

	public abstract PlayerSkinData createNewData(String value);

	@Nullable
	protected PlayerSkinData getFromCache(String value) {
		return this.cache.get(value);
	}

	protected void putToCache(String value, PlayerSkinData data) {
		this.cache.put(value, data);
	}

	protected Identifier getSkinId(String value) {
		return this.getId(value);
	}

	protected abstract Identifier getId(String value);
}
