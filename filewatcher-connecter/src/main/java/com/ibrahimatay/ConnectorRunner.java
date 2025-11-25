package com.ibrahimatay;

import org.apache.kafka.connect.source.SourceRecord;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConnectorRunner {

    public static void main(String[] args) throws InterruptedException, IOException {
        // Setup test directory
        Path testDir = Path.of("test-watch-dir");
        if (!Files.exists(testDir)) {
            Files.createDirectory(testDir);
        }
        System.out.println("Watching directory: " + testDir.toAbsolutePath());

        // Configure connector
        Map<String, String> props = new HashMap<>();
        props.put(FileWatcherConfig.DIR_PATH_CONFIG, testDir.toAbsolutePath().toString());
        props.put(FileWatcherConfig.TOPIC_CONFIG, "test-topic");
        props.put(FileWatcherConfig.POLL_INTERVAL_MS_CONFIG, "1000");

        FileWatcherSourceTask task = new FileWatcherSourceTask();
        task.start(props);

        // Start a thread to simulate file changes
        new Thread(() -> {
            try {
                Thread.sleep(2000);
                System.out.println("Creating file1.txt...");
                Files.writeString(testDir.resolve("file1.txt"), "Hello Kafka!");

                Thread.sleep(2000);
                System.out.println("Modifying file1.txt...");
                Files.writeString(testDir.resolve("file1.txt"), "Hello Kafka! Modified.");

                Thread.sleep(2000);
                System.out.println("Creating file2.txt...");
                Files.writeString(testDir.resolve("file2.txt"), "Another file.");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        // Run poll loop
        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime < 10000) { // Run for 10 seconds
            List<SourceRecord> records = task.poll();
            if (records != null) {
                for (SourceRecord record : records) {
                    System.out.println("Received Record:");
                    System.out.println("  Topic: " + record.topic());
                    System.out.println("  Value: " + record.value());
                }
            }
            Thread.sleep(100);
        }

        task.stop();
        System.out.println("Runner finished.");
    }
}
