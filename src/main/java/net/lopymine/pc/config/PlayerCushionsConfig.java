package net.lopymine.pc.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.io.File;
import java.util.concurrent.CompletableFuture;
import lombok.*;
import net.lopymine.mossylib.loader.MossyLoader;
import net.lopymine.mossylib.utils.*;
import net.lopymine.pc.PlayerCushions;
import org.slf4j.*;
import static net.lopymine.mossylib.utils.CodecUtils.option;

@Getter
@Setter
@AllArgsConstructor
public class PlayerCushionsConfig {

	public static final Codec<PlayerCushionsConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			option("mod_enabled", true, Codec.BOOL, PlayerCushionsConfig::isModEnabled),
			option("debug_log_enabled", false, Codec.BOOL, PlayerCushionsConfig::isDebugLogEnabled),
			option("standard_skin_data", "", Codec.STRING, PlayerCushionsConfig::getStandardSkinValue),
			option("executor_threads_count", 6, Codec.INT, PlayerCushionsConfig::getParallelTasksCount)
	).apply(instance, PlayerCushionsConfig::new));

	private static final File CONFIG_FILE = MossyLoader.getConfigDir().resolve(PlayerCushions.MOD_ID + ".json5").toFile();
	private static final Logger LOGGER = LoggerFactory.getLogger(PlayerCushions.MOD_NAME + "/Config");
	private static PlayerCushionsConfig INSTANCE;

	private boolean modEnabled;
	private boolean debugLogEnabled;
	private String standardSkinValue;
	private int parallelTasksCount;

	private PlayerCushionsConfig() {
		throw new IllegalArgumentException();
	}

	public static PlayerCushionsConfig getInstance() {
		return INSTANCE == null ? reload() : INSTANCE;
	}

	public static PlayerCushionsConfig reload() {
		return INSTANCE = PlayerCushionsConfig.read();
	}

	public static PlayerCushionsConfig getNewInstance() {
		return CodecUtils.parseNewInstanceHacky(CODEC);
	}

	private static PlayerCushionsConfig read() {
		return ConfigUtils.readConfig(CODEC, CONFIG_FILE, LOGGER);
	}

	public void saveAsync() {
		CompletableFuture.runAsync(this::save);
	}

	public void save() {
		ConfigUtils.saveConfig(this, CODEC, CONFIG_FILE, LOGGER);
	}
}
