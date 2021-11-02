package com.mcmiddleearth.mcmescripts;

public enum Permission {

    USER    ("mcmescripts.user"),
    ADMIN   ("mcmescripts.admin");

    private final String node;

    Permission(String node) {
        this.node = node;
    }

    public String getNode() {
        return node;
    }
}
