package net.lopymine.pc.data;

import java.util.Locale;
import lombok.Getter;
import net.lopymine.pc.PlayerCushions;
import net.minecraft.network.chat.Component;

@Getter
public enum LoadingState {

	ERROR, // Y
	CRITICAL_ERROR, // X
	NOT_FOUND, // X
	DESTROYED, // X
	NOT_DOWNLOADED, // Y
	WAITING_DOWNLOADING, // X
	DOWNLOADING, // X
	REGISTERING, // X
	DOWNLOADED; // X

	public Component getText() {
		return PlayerCushions.text("modmenu.option.standard_skin_type.result.%s".formatted(this.name().toLowerCase(Locale.ROOT)));
	}
}
