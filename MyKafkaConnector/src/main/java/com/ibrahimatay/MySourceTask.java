package com.ibrahimatay;

import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.source.*;
import java.lang.*;
import java.util.*;
public class MySourceTask extends SourceTask {
    @Override
    public String version() {
        return "1.0";
    }

    @Override
    public void start(java.util.Map<String, String> map) {

    }

    @Override
    public java.util.List<SourceRecord> poll() throws InterruptedException {
        List<SourceRecord> records = new ArrayList<>();

        SourceRecord record = new SourceRecord(
                null,
                null,
                "my-kafka-topic",
                Schema.STRING_SCHEMA,
                "key",
                Schema.STRING_SCHEMA,
                "value"
        );

        records.add(record);
        return records;
    }

    @Override
    public void stop() {
        // Cleanup logic
    }
}
