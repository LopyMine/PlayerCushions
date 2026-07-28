package net.lopymine.pc.yacl;

import net.lopymine.mossylib.yacl.api.SimpleYACLScreen;
import net.lopymine.pc.PlayerCushions;
import net.lopymine.pc.config.PlayerCushionsConfig;
import net.lopymine.pc.yacl.category.*;
import net.minecraft.client.gui.screens.Screen;

public class YACLConfigurationScreen {

	public static Screen createScreen(Screen parent) {
		PlayerCushionsConfig defConfig = PlayerCushionsConfig.getNewInstance();
		PlayerCushionsConfig config = PlayerCushionsConfig.getInstance();

		return SimpleYACLScreen.startBuilder(PlayerCushions.MOD_ID, parent, config::save)
				.categories(GeneralCategory.get(defConfig, config))
				.build();
	}
}


