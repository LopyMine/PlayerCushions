package net.lopymine.pc.yacl.category;

import dev.isxander.yacl3.api.*;
import lombok.experimental.ExtensionMethod;
import net.lopymine.mossylib.yacl.api.*;
import net.lopymine.mossylib.yacl.extension.SimpleOptionExtension;
import net.lopymine.pc.config.PlayerCushionsConfig;
import net.lopymine.pc.thread.PlayerCushionsTaskExecutor;

@ExtensionMethod(SimpleOptionExtension.class)
public class GeneralCategory {

	public static SimpleCategory get(PlayerCushionsConfig defConfig, PlayerCushionsConfig config) {
		return SimpleCategory.startBuilder("general")
				.groups(getMainGroup(defConfig, config))
				.groups(getThreadGroup(defConfig, config));
	}

	private static SimpleGroup getMainGroup(PlayerCushionsConfig defConfig, PlayerCushionsConfig config) {
		return SimpleGroup.startBuilder("main")
				.options(
						SimpleOption.<Boolean>startBuilder("mod_enabled")
								.withBinding(defConfig.isModEnabled(), config::isModEnabled, config::setModEnabled, true)
								.withDescription(SimpleContent.NONE)
								.withController(),
						SimpleOption.<Boolean>startBuilder("debug_log_enabled")
								.withBinding(defConfig.isDebugLogEnabled(), config::isDebugLogEnabled, config::setDebugLogEnabled, true)
								.withDescription(SimpleContent.NONE)
								.withController()
				);
	}

	public static SimpleGroup getThreadGroup(PlayerCushionsConfig defConfig, PlayerCushionsConfig config) {
		return SimpleGroup.startBuilder("parallel_tasks")
				.options(
						SimpleOption.<Integer>startBuilder("parallel_tasks_count")
								.withBinding(defConfig.getParallelTasksCount(), config::getParallelTasksCount, (i) -> {
									config.setParallelTasksCount(i);
									PlayerCushionsTaskExecutor.reload();
								}, false)
								.withDescription(SimpleContent.NONE)
								.withController(1, 50, 1)
				);
	}

}
