package net.lopymine.pc.thread;

import java.util.List;
import java.util.concurrent.*;
import net.lopymine.pc.config.PlayerCushionsConfig;

public class PlayerCushionsTaskExecutor {

	public static ExecutorService MAIN_EXECUTOR = Executors.newFixedThreadPool(PlayerCushionsConfig.getInstance().getParallelTasksCount());

	public static void reload() {
		int threadsCount = PlayerCushionsConfig.getInstance().getParallelTasksCount();
		List<Runnable> runnables = MAIN_EXECUTOR.shutdownNow();
		MAIN_EXECUTOR = Executors.newFixedThreadPool(threadsCount);
		for (Runnable runnable : runnables) {
			MAIN_EXECUTOR.submit(runnable);
		}
	}

	public static void stop() {
		MAIN_EXECUTOR.shutdown();
	}

	public static CompletableFuture<Void> execute(Runnable runnable) {
		return CompletableFuture.runAsync(runnable, MAIN_EXECUTOR);
	}
}
