package com.ibrahimatay;

import org.apache.kafka.common.config.*;

import java.util.*;

public class MyConnectorConfig extends AbstractConfig {
    public MyConnectorConfig(Map<String, String> props) {
        super(CONFIG_DEF, props);
    }

    private static final ConfigDef CONFIG_DEF = new ConfigDef()
            .define("my.config.param", ConfigDef.Type.STRING, ConfigDef.Importance.HIGH, "Description of the config param");
}

