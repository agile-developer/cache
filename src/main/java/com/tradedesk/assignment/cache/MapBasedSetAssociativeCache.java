package com.tradedesk.assignment.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class MapBasedSetAssociativeCache {

    private final int numSets;
    private final Map<Integer, FixedSizeCache> setAssociativeCache;

    public MapBasedSetAssociativeCache(int numSets, int setCapacity, CacheType cacheType) {

        this.numSets = numSets;
        this.setAssociativeCache = new HashMap<>();
        IntStream.range(0, numSets).forEach(index -> {
            var cache = switch (cacheType) {
                case LRU -> new LeastRecentlyUsedCache(setCapacity);
                case LFU -> new LeastFrequentlyUsedCache(setCapacity);
            };
            setAssociativeCache.put(index, cache);
        });
    }

    public int get(int key) {

        return setAssociativeCache.get(calculateSetIndex(key)).get(key);
    }

    public void put(int key, int value) {

        setAssociativeCache.get(calculateSetIndex(key)).put(key, value);
    }

    private int calculateSetIndex(int key) {

        return key % numSets;
    }
}
