package com.tradedesk.assignment.cache;

public enum CacheType {

    LRU("Least Recently Used"), LFU("Least Frequently Used");

    final String description;

    CacheType(String description) {
        this.description = description;
    }
}
