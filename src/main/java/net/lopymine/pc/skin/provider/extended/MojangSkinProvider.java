package net.lopymine.pc.skin.provider.extended;

import java.util.*;
import java.util.stream.Collectors;
import net.lopymine.pc.PlayerCushions;
import net.lopymine.pc.api.*;
import net.lopymine.pc.data.PlayerSkinData;
import net.lopymine.pc.skin.data.ParsedSkinData;
import net.lopymine.pc.skin.provider.StandardSkinProvider;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.Nullable;

public class MojangSkinProvider extends StandardSkinProvider {

	private static final MojangSkinProvider INSTANCE = new MojangSkinProvider();

	private MojangSkinProvider() {
		super(true);
	}

	public static MojangSkinProvider getInstance() {
		return MojangSkinProvider.INSTANCE;
	}

	@Override
	protected Response<ParsedSkinData> loadDataFromAPI(String value) {
		return MojangAPI.getSkinData(value.toLowerCase());
	}

	@Override
	public PlayerSkinData createNewData(String value) {
		return PlayerSkinData.create(value);
	}

	@Override
	protected @Nullable PlayerSkinData getFromCache(String value) {
		return super.getFromCache(value.toLowerCase());
	}

	@Override
	protected void putToCache(String value, PlayerSkinData data) {
		super.putToCache(value.toLowerCase(), data);
	}

	@Override
	public Set<String> getLoadedKeys() {
		return this.getCache().values().stream().map(PlayerSkinData::getNickname).filter(Objects::nonNull).collect(Collectors.toSet());
	}

	@Override
	protected Identifier getId(String value) {
		return PlayerCushions.getDataTextureId("mojang_api/%s/%s".formatted("skin", value.toLowerCase()));
	}

	@Override
	public boolean canProcess(String value) {
		if (value == null) {
			return false;
		}

		int length = value.length();
		if (length < 2 || length > 16) {
			return false;
		}

		for (int i = 0; i < length; i++) {
			char c = value.charAt(i);
			if ((c == '_') || (c >= '0' && c <= '9') || (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z')) {
				continue;
			}
			return false;
		}

		return true;
	}
}
