package net.lopymine.pc.skin.provider.extended;

import net.lopymine.pc.PlayerCushions;
import net.lopymine.pc.api.*;
import net.lopymine.pc.data.PlayerSkinData;
import net.lopymine.pc.skin.data.ParsedSkinData;
import net.lopymine.pc.skin.provider.StandardSkinProvider;
import net.minecraft.resources.Identifier;

public class NameMCSkinProvider extends StandardSkinProvider {

	private static final NameMCSkinProvider INSTANCE = new NameMCSkinProvider();

	private NameMCSkinProvider() {
		super(false);
	}

	public static NameMCSkinProvider getInstance() {
		return NameMCSkinProvider.INSTANCE;
	}

	@Override
	protected Response<ParsedSkinData> loadDataFromAPI(String value) {
		return NameMCAPI.getSkinData(value);
	}

	@Override
	public PlayerSkinData createNewData(String value) {
		return PlayerSkinData.create("NameMC");
	}

	@Override
	protected Identifier getId(String value) {
		return PlayerCushions.getDataTextureId("name_mc/%s/%s".formatted("skin", value.toLowerCase()));
	}

	@Override
	public boolean canProcess(String value) {
		if (value == null || value.length() != 16) {
			return false;
		}

		for (int i = 0; i < 16; i++) {
			char c = value.charAt(i);
			if ((c >= 'a' && c <= 'z') || (c >= '0' && c <= '9')) {
				continue;
			}
			return false;
		}

		return true;
	}
}
