package com.tradedesk.assignment.cache;

import java.util.stream.IntStream;

public class ArrayBasedSetAssociativeCache {

    private final int numSets;
    private final FixedSizeCache[] setAssociativeCache;

    public ArrayBasedSetAssociativeCache(int numSets, int setCapacity, CacheType cacheType) {
        this.numSets = numSets;
        setAssociativeCache = new FixedSizeCache[numSets];
        IntStream.range(0, numSets).forEach(
            index -> setAssociativeCache[index] = switch (cacheType) {
                case LRU -> new LeastRecentlyUsedCache(setCapacity);
                case LFU -> new LeastFrequentlyUsedCache(setCapacity);
            }
        );
    }

    public int get(int key) {

        return setAssociativeCache[calculateSetIndex(key)].get(key);
    }

    public void put(int key, int value) {

        setAssociativeCache[calculateSetIndex(key)].put(key, value);
    }

    private int calculateSetIndex(int key) {

        return key % numSets;
    }
}
