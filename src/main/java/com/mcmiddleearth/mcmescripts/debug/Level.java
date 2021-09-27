package com.mcmiddleearth.mcmescripts.debug;

public enum Level {
    VERBOSE (0),
    INFO    (1),
    WARNING (2),
    SEVERE  (3),
    CRITICAL(4);

    private final int level;

    Level(int debugLevel) {
        this.level = debugLevel;
    }

    public int getDebugLevel() {
        return level;
    }

    public static Level next(Level previous) {
        if(previous == null) previous = Level.CRITICAL;
        switch(previous) {
            case VERBOSE:
                return Level.INFO;
            case INFO:
                return Level.WARNING;
            case WARNING:
                return Level.SEVERE;
            case SEVERE:
                return Level.CRITICAL;
            case CRITICAL:
            default:
                return Level.VERBOSE;
        }
    }
}
