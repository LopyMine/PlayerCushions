package net.lopymine.pc.skin.provider;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import net.lopymine.pc.data.PlayerSkinData;
import org.jetbrains.annotations.*;

public interface SkinProvider {

	@Nullable
	PlayerSkinData getOrLoadData(String value);

	Set<String> getLoadedKeys();

	Collection<PlayerSkinData> getLoadedData();

	CompletableFuture<Void> reloadAll();

	CompletableFuture<Void> reloadOne(String value);

	boolean canProcess(String value);
}
