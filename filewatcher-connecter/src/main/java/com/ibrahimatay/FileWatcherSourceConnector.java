package com.ibrahimatay;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.connector.Task;
import org.apache.kafka.connect.source.SourceConnector;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FileWatcherSourceConnector extends SourceConnector {

    private Map<String, String> configProps;

    @Override
    public void start(Map<String, String> props) {
        this.configProps = props;
    }

    @Override
    public Class<? extends Task> taskClass() {
        return FileWatcherSourceTask.class;
    }

    @Override
    public List<Map<String, String>> taskConfigs(int maxTasks) {
        List<Map<String, String>> configs = new ArrayList<>();
        // This connector only supports one task effectively as it watches a single
        // directory
        // In a real distributed scenario, we might shard directories, but for now 1
        // task.
        configs.add(configProps);
        return configs;
    }

    @Override
    public void stop() {
        // Nothing to do
    }

    @Override
    public ConfigDef config() {
        return FileWatcherConfig.config();
    }

    @Override
    public String version() {
        return "1.0";
    }
}
