package com.tradedesk.assignment.cache;

import java.util.LinkedHashMap;
import java.util.Map;

public class SimpleLeastRecentlyUsedCache implements FixedSizeCache {

    private final int capacity;
    private final Map<Integer, Integer> cache;

    public SimpleLeastRecentlyUsedCache(int capacity) {

        this.capacity = capacity;
        cache = new LinkedHashMap<>() {
            @Override
            protected boolean removeEldestEntry(Map.Entry<Integer, Integer> eldest) {

                return this.size() == capacity;
            }
        };
    }

    @Override
    public int get(int key) {

        return cache.get(key);
    }

    @Override
    public void put(int key, int value) {

        cache.put(key, value);
    }

    @Override
    public int capacity() {

        return capacity;
    }

    @Override
    public CacheType type() {
        return null;
    }

    @Override
    public void debug() {

        System.out.println("-----");
        System.out.println("Cache contents: ");
        cache.forEach((key, value) -> System.out.printf("{ key: %s, value: %s} ", key, value));
        System.out.println();
        System.out.println("-----");
    }
}
