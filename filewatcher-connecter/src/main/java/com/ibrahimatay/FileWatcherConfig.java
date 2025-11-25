package com.ibrahimatay;

import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigDef.Importance;
import org.apache.kafka.common.config.ConfigDef.Type;

import java.util.Map;

public class FileWatcherConfig extends AbstractConfig {

    public static final String DIR_PATH_CONFIG = "file.watcher.dir";
    private static final String DIR_PATH_DOC = "Directory to watch for file changes";

    public static final String TOPIC_CONFIG = "topic";
    private static final String TOPIC_DOC = "Topic to publish file data to";

    public static final String POLL_INTERVAL_MS_CONFIG = "poll.interval.ms";
    private static final String POLL_INTERVAL_MS_DOC = "Poll interval in milliseconds";
    public static final int POLL_INTERVAL_MS_DEFAULT = 1000;

    public FileWatcherConfig(Map<String, String> originals) {
        super(config(), originals);
    }

    public static ConfigDef config() {
        return new ConfigDef()
                .define(DIR_PATH_CONFIG, Type.STRING, Importance.HIGH, DIR_PATH_DOC)
                .define(TOPIC_CONFIG, Type.STRING, Importance.HIGH, TOPIC_DOC)
                .define(POLL_INTERVAL_MS_CONFIG, Type.INT, POLL_INTERVAL_MS_DEFAULT, Importance.LOW,
                        POLL_INTERVAL_MS_DOC);
    }

    public String getDirPath() {
        return getString(DIR_PATH_CONFIG);
    }

    public String getTopic() {
        return getString(TOPIC_CONFIG);
    }

    public int getPollInterval() {
        return getInt(POLL_INTERVAL_MS_CONFIG);
    }
}
