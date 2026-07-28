package net.lopymine.pc.data;

import lombok.*;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.Nullable;

@Getter
@Setter
public class PlayerSkinData {

	private String nickname;
	private LoadingState state = LoadingState.NOT_DOWNLOADED;
	@Nullable
	private Identifier skinId;

	private PlayerSkinData(String nickname) {
		this.nickname = nickname;
	}

	public static PlayerSkinData create(String nickname) {
		return new PlayerSkinData(nickname);
	}

	public boolean canStartDownloading() {
		return this.state == LoadingState.ERROR || this.state == LoadingState.NOT_DOWNLOADED;
	}

	public void destroy() {
		this.setState(LoadingState.DESTROYED);

		Identifier skinSprite = this.skinId;
		this.skinId = null;

		if (skinSprite != null) {
			Minecraft.getInstance().getTextureManager().release(skinSprite);
		}
	}

}
