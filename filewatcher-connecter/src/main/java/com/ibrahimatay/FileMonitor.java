package com.ibrahimatay;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class FileMonitor {
    private static final Logger log = LoggerFactory.getLogger(FileMonitor.class);

    private final Path directory;
    private WatchService watchService;
    private boolean running;

    public FileMonitor(String directoryPath) throws IOException {
        this.directory = Paths.get(directoryPath);
        if (!Files.exists(this.directory)) {
            throw new IllegalArgumentException("Directory does not exist: " + directoryPath);
        }
        this.watchService = FileSystems.getDefault().newWatchService();
        this.directory.register(watchService, StandardWatchEventKinds.ENTRY_CREATE,
                StandardWatchEventKinds.ENTRY_MODIFY);
        this.running = true;
    }

    public List<Path> poll(long timeoutMs) {
        List<Path> changedFiles = new ArrayList<>();
        if (!running) {
            return changedFiles;
        }

        try {
            WatchKey key = watchService.poll(timeoutMs, TimeUnit.MILLISECONDS);
            if (key != null) {
                for (WatchEvent<?> event : key.pollEvents()) {
                    WatchEvent.Kind<?> kind = event.kind();

                    if (kind == StandardWatchEventKinds.OVERFLOW) {
                        continue;
                    }

                    @SuppressWarnings("unchecked")
                    WatchEvent<Path> ev = (WatchEvent<Path>) event;
                    Path filename = ev.context();
                    Path child = directory.resolve(filename);

                    // Ignore hidden files or temporary files if needed, for now accept all
                    if (Files.isRegularFile(child)) {
                        changedFiles.add(child);
                    }
                }
                boolean valid = key.reset();
                if (!valid) {
                    log.warn("WatchKey no longer valid. Stopping monitor.");
                    running = false;
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.info("Interrupted while polling for file changes");
        }
        return changedFiles;
    }

    public void close() {
        running = false;
        try {
            if (watchService != null) {
                watchService.close();
            }
        } catch (IOException e) {
            log.error("Error closing WatchService", e);
        }
    }
}
