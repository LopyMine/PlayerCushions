package net.lopymine.pc.data;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import net.lopymine.pc.skin.provider.SkinProvider;
import net.lopymine.pc.skin.provider.extended.MojangSkinProvider;
import net.lopymine.pc.tags.TagsSkinProviders;
import org.jetbrains.annotations.Nullable;

public class PlayerCushionsDataManager {

	@Nullable
	public static PlayerSkinData getData(String nickname) {
		if (TagsSkinProviders.isProvider(nickname)) {
			return TagsSkinProviders.loadDataFromProvider(nickname);
		}
		return MojangSkinProvider.getInstance().getOrLoadData(nickname);
	}

	public static Set<String> getAllLoadedKeys() {
		Set<String> loaded = new HashSet<>();

		for (SkinProvider value : TagsSkinProviders.getSkinProvidersIds().values()) {
			loaded.addAll(value.getLoadedKeys());
		}

		loaded.addAll(MojangSkinProvider.getInstance().getLoadedKeys());

		return loaded;
	}

	public static CompletableFuture<Float> reloadData(Consumer<Float> action) {
		List<SkinProvider> providers = new ArrayList<>(TagsSkinProviders.getSkinProvidersIds().values());
		providers.add(MojangSkinProvider.getInstance());

		Set<CompletableFuture<?>> list = new HashSet<>();
		long startMs = System.currentTimeMillis();

		for (SkinProvider provider : providers) {
			list.add(provider.reloadAll());
		}

		return CompletableFuture.allOf(list.toArray(new CompletableFuture[0])).thenApply((__) -> {
			action.accept((System.currentTimeMillis() - startMs) / 1000F);
			return null;
		});
	}

	@Nullable
	public static CompletableFuture<Float> reloadData(String value, Consumer<Float> action) {
		long startMs = System.currentTimeMillis();

		SkinProvider skinProvider = TagsSkinProviders.getProviderFor(value);

		CompletableFuture<Void> completableFuture =
				skinProvider == null
						?
						MojangSkinProvider.getInstance().reloadOne(value)
						:
						skinProvider.reloadOne(value);

		if (completableFuture == null) {
			return null;
		}

		return completableFuture.thenApply((__) -> {
			action.accept((System.currentTimeMillis() - startMs) / 1000F);
			return null;
		});
	}
}
