package com.mcmiddleearth.mcmescripts;

public enum ConfigKeys {

    SCRIPT_CHECKER_PERIOD ("scriptCheckerPeriod"),
    TRIGGER_CHECKER_PERIOD ("triggerCheckerPeriod"),
    START_UP_DELAY ("startUpDelay");

    private final String key;

    ConfigKeys(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
