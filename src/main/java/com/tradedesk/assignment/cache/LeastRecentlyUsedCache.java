package com.tradedesk.assignment.cache;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class LeastRecentlyUsedCache implements FixedSizeCache {

    private final int capacity;
    private final DoublyLinkedList doublyLinkedList;
    private final Map<Integer, CacheNode> cache;

    public LeastRecentlyUsedCache(int capacity) {

        this.capacity = capacity;
        this.doublyLinkedList = new DoublyLinkedList();
        this.cache = new HashMap<>();
    }

    public int get(int key) {

        if (!cache.containsKey(key)) return -1;
        var newMru = cache.get(key);
        doublyLinkedList.removeFromCurrentPosition(newMru);
        doublyLinkedList.insertAsMostRecentlyUsed(newMru);
        return newMru.value;
    }

    public void put(int key, int value) {

        if (capacity == 0) return;

        var newNode = new CacheNode(key, value, null, null);
        var existingNode = cache.get(key);

        if (existingNode != null) {
            cache.put(key, newNode);
            doublyLinkedList.removeFromCurrentPosition(existingNode);
            doublyLinkedList.insertAsMostRecentlyUsed(newNode);
            return;
        }

        if (cache.size() == capacity) {
            cache.remove(doublyLinkedList.leastRecentlyUsed.next.key);
            doublyLinkedList.removeFromCurrentPosition(doublyLinkedList.leastRecentlyUsed.next);
        }
        cache.put(key, newNode);
        doublyLinkedList.insertAsMostRecentlyUsed(newNode);
    }

    public Map<Integer, CacheNode> cache() {
        return Collections.unmodifiableMap(cache);
    }

    public int capacity() {
        return capacity;
    }

    DoublyLinkedList getDoublyLinkedList() {
        return doublyLinkedList;
    }

    @Override
    public CacheType type() {
        return CacheType.LRU;
    }

    @Override
    public void debug() {

        System.out.println("-----");
        System.out.println("Cache contents");
        cache.forEach((key, cacheNode) -> System.out.printf("{key: %s, value: %s} ", key, cacheNode.value));
        System.out.println();
        System.out.println("-----");

        System.out.println("----");
        System.out.println("LRU list contents:");
        doublyLinkedList.debug();
        System.out.println();
        System.out.println("-----");
    }
}
