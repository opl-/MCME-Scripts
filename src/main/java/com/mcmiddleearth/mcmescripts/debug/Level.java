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
        return switch (previous) {
            case VERBOSE -> Level.INFO;
            case INFO -> Level.WARNING;
            case WARNING -> Level.SEVERE;
            case SEVERE -> Level.CRITICAL;
            default -> Level.VERBOSE;
        };
    }

    public java.util.logging.Level getLoggerLevel() {
        return switch (level) {
            case 0 -> java.util.logging.Level.ALL;
            case 1 -> java.util.logging.Level.INFO;
            case 2 -> java.util.logging.Level.WARNING;
            default -> java.util.logging.Level.SEVERE;
        };
    }
}
