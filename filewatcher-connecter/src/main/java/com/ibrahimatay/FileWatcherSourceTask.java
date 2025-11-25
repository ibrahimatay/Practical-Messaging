package com.ibrahimatay;

import com.ibrahimatay.FileMonitor;
import com.ibrahimatay.FileWatcherConfig;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.SchemaBuilder;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.source.SourceRecord;
import org.apache.kafka.connect.source.SourceTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class FileWatcherSourceTask extends SourceTask {
    private static final Logger log = LoggerFactory.getLogger(FileWatcherSourceTask.class);

    private FileWatcherConfig config;
    private FileMonitor monitor;
    private String topic;

    private static final Schema VALUE_SCHEMA = SchemaBuilder.struct()
            .name("com.example.kafka.connect.filewatcher.FileContent")
            .field("filename", Schema.STRING_SCHEMA)
            .field("path", Schema.STRING_SCHEMA)
            .field("content", Schema.STRING_SCHEMA)
            .field("timestamp", Schema.INT64_SCHEMA)
            .build();

    @Override
    public String version() {
        return "1.0";
    }

    @Override
    public void start(Map<String, String> props) {
        config = new FileWatcherConfig(props);
        topic = config.getTopic();
        try {
            monitor = new FileMonitor(config.getDirPath());
        } catch (IOException e) {
            throw new RuntimeException("Failed to start FileMonitor", e);
        }
    }

    @Override
    public List<SourceRecord> poll() throws InterruptedException {
        List<Path> changes = monitor.poll(config.getPollInterval());
        if (changes.isEmpty()) {
            return null;
        }

        List<SourceRecord> records = new ArrayList<>();
        for (Path file : changes) {
            try {
                String content = Files.readString(file, StandardCharsets.UTF_8);
                Struct value = new Struct(VALUE_SCHEMA)
                        .put("filename", file.getFileName().toString())
                        .put("path", file.toAbsolutePath().toString())
                        .put("content", content)
                        .put("timestamp", System.currentTimeMillis());

                Map<String, String> sourcePartition = Collections.singletonMap("filename",
                        file.getFileName().toString());
                Map<String, Long> sourceOffset = Collections.singletonMap("timestamp", System.currentTimeMillis());

                records.add(new SourceRecord(
                        sourcePartition,
                        sourceOffset,
                        topic,
                        VALUE_SCHEMA,
                        value));
            } catch (IOException e) {
                log.error("Failed to read file: " + file, e);
            }
        }
        return records;
    }

    @Override
    public void stop() {
        if (monitor != null) {
            monitor.close();
        }
    }
}
