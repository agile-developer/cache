package com.tradedesk.assignment.cache;

import java.util.HashMap;
import java.util.Map;

public class LRUCacheV1 {

    private final int capacity;
    private final CacheNode leastRecentlyUsed;
    private final CacheNode mostRecentlyUsed;
    private final Map<Integer, CacheNode> cache;

    public LRUCacheV1(int capacity) {

        this.capacity = capacity;
        this.leastRecentlyUsed = new CacheNode(-1, -1, null, null);
        this.mostRecentlyUsed = new CacheNode(-1, -1, null, null);
        this.leastRecentlyUsed.next = this.mostRecentlyUsed;
        this.mostRecentlyUsed.previous = this.leastRecentlyUsed;
        this.cache = new HashMap<>();
    }

    public int get(int key) {

        if (!cache.containsKey(key)) return -1;
        var newMru = cache.get(key);
        removeFromCurrentPosition(newMru);
        insertAsMostRecentlyUsed(newMru);
        return newMru.value;
    }

    public void put(int key, int value) {

        var newNode = new CacheNode(key, value, null, null);
        var existingNode = cache.get(key);

        if (existingNode != null) {
            cache.put(key, newNode);
            removeFromCurrentPosition(existingNode);
            insertAsMostRecentlyUsed(newNode);
            return;
        }

        if (cache.size() == capacity) {
            cache.remove(leastRecentlyUsed.next.key);
            removeFromCurrentPosition(leastRecentlyUsed.next);
        }
        cache.put(key, newNode);
        insertAsMostRecentlyUsed(newNode);
    }

    private void insertAsMostRecentlyUsed(CacheNode node) {

        var lastMru = mostRecentlyUsed.previous;
        mostRecentlyUsed.previous = node;
        node.previous = lastMru;
        node.next = mostRecentlyUsed;
        lastMru.next = node;
    }

    private void removeFromCurrentPosition(CacheNode node) {

        var removedNodesPrevious = node.previous;
        var removedNodesNext = node.next;
        removedNodesPrevious.next = removedNodesNext;
        removedNodesNext.previous = removedNodesPrevious;
    }

    public int linkedListSize() {

        var size = 0;
        var next = leastRecentlyUsed.next;
        System.out.println("-----");
        while (next != mostRecentlyUsed) {
            size++;
            System.out.println("Key: " + next.key);
            System.out.println("Key: " + next.value);
            next = next.next;
        }
        System.out.println("-----");
        return size;
    }

    public CacheNode getLeastRecentlyUsed() {
        return leastRecentlyUsed;
    }

    public CacheNode getMostRecentlyUsed() {
        return mostRecentlyUsed;
    }

    public Map<Integer, CacheNode> cache() {
        return cache;
    }

}
