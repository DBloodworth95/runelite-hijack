package com.runelitehijack.client;

import net.runelite.client.RuneLite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class ClientHijack {
    private static final Logger logger = LoggerFactory.getLogger(ClientHijack.class);
    private static final long SLEEP_INTERVAL_MS = 100;

    public ClientHijack() {
        logger.info("Looking for runelite injector..");
        hijackClient();
    }

    private void hijackClient() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            while (RuneLite.getInjector() == null) {
                try {
                    Thread.sleep(SLEEP_INTERVAL_MS);
                } catch (InterruptedException ex) {
                    logger.error("Thread interrupted while sleeping.", ex);
                    Thread.currentThread().interrupt();
                }
            }
            logger.info("Injector found!");
            RuneLite.getInjector().getInstance(HijackedClientBackup.class).start();
        });

        executorService.shutdown();
    }
}
