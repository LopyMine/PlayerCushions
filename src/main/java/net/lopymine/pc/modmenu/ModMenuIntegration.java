package net.lopymine.pc.modmenu;

import net.lopymine.mossylib.modmenu.AbstractModMenuIntegration;
import net.lopymine.pc.PlayerCushions;
import net.lopymine.pc.yacl.YACLConfigurationScreen;
import net.minecraft.client.gui.screens.Screen;

public class ModMenuIntegration extends AbstractModMenuIntegration {

	@Override
	protected String getModId() {
		return PlayerCushions.MOD_ID;
	}

	@Override
	protected Screen createConfigScreen(Screen screen) {
		return YACLConfigurationScreen.createScreen(screen);
	}
}
