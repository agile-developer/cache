package com.tradedesk.assignment.cache;

public interface FixedSizeCache {

    int get(int key);

    void put(int key, int value);

    int capacity();

    CacheType type();

    void debug();
}
