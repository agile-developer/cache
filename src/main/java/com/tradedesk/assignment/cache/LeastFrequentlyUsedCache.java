package com.tradedesk.assignment.cache;

import java.util.*;

public class LeastFrequentlyUsedCache implements FixedSizeCache {

    private final int capacity;
    private final Map<Integer, CacheNode> cache;
    private final Map<Integer, Integer> frequencyCounter;
    private final SortedMap<Integer, DoublyLinkedList> frequencyLru;

    public LeastFrequentlyUsedCache(int capacity) {
        this.capacity = capacity;
        this.cache = new HashMap<>();
        this.frequencyCounter = new HashMap<>();
        this.frequencyLru = new TreeMap<>();
    }

    public int get(int key) {

        if (!cache.containsKey(key)) return -1;
        var node = cache.get(key);

        // Get the current counter of this key
        int currentKeyCount = frequencyCounter.get(node.key);

        // Get the DLL that contains this key's node from the frequencyLru map
        var currentLruList = frequencyLru.get(currentKeyCount);

        // Remove the cache node from currentLruList
        currentLruList.removeFromCurrentPosition(node);
        // If the list at this position has become empty, purge it from the map
        if (currentLruList.isEmpty()) {
            frequencyLru.remove(currentKeyCount);
        }

        // Update counter for key
        var newKeyCount = currentKeyCount + 1;
        frequencyCounter.put(node.key, newKeyCount);

        // Put the cache node in the frequencyLru map against the new count value
        var nextLruList = frequencyLru.get(newKeyCount);
        if (nextLruList == null) {
            nextLruList = new DoublyLinkedList();
            frequencyLru.put(newKeyCount, nextLruList);
        }
        nextLruList.insertAsMostRecentlyUsed(node);

        return node.value;
    }

    public void put(int key, int value) {

        if (capacity == 0) return;

        var newNode = new CacheNode(key, value, null, null);
        var currentNode = cache.get(key);
        if (currentNode != null) {
            // Update cache with newNode
            cache.put(key, newNode);

            // Get the current counter of this key
            int currentKeyCount = frequencyCounter.get(key);

            // Get the DLL that contains this key's node from the frequencyLru map
            var currentLruList = frequencyLru.get(currentKeyCount);

            // Remove the currentNode from currentLruList
            currentLruList.removeFromCurrentPosition(currentNode);
            // If the list at this position has become empty, purge it from the map
            if (currentLruList.isEmpty()) {
                frequencyLru.remove(currentKeyCount);
            }

            // Update counter for key
            var newKeyCount = currentKeyCount + 1;
            frequencyCounter.put(key, newKeyCount);

            // Put the newNode in the frequencyLru map against the new count value
            var nextLruList = frequencyLru.get(newKeyCount);
            if (nextLruList == null) {
                nextLruList = new DoublyLinkedList();
                frequencyLru.put(newKeyCount, nextLruList);
            }
            nextLruList.insertAsMostRecentlyUsed(newNode);
            return;
        }

        if (cache.size() == capacity) {
            // Evict LFU node. This is given by getting the lowest key in the frequencyLru map, which should not be empty at this point
            var lowestFrequency = frequencyLru.firstKey();
            var lowestFrequencyLruList = frequencyLru.get(lowestFrequency);
            // Get the LRU node from this list
            var nodeToEvict = lowestFrequencyLruList.leastRecentlyUsed.next;
            // Evict from this list
            lowestFrequencyLruList.removeFromCurrentPosition(nodeToEvict);
            // If the list at this position has become empty, purge it from the map
            if (lowestFrequencyLruList.isEmpty()) {
                frequencyLru.remove(lowestFrequency);
            }
            // Evict from frequencyCounter
            frequencyCounter.remove(nodeToEvict.key);
            // Evict from cache
            cache.remove(nodeToEvict.key);
        }

        // Update cache with newNode
        cache.put(key, newNode);
        // Increment counter for new node's key
        frequencyCounter.put(key, 1);
        // Add new node to the LRU list of node's frequency
        var lruList = frequencyLru.get(1);
        if (lruList == null) {
            lruList = new DoublyLinkedList();
            frequencyLru.put(1, lruList);
        }
        lruList.insertAsMostRecentlyUsed(newNode);
    }

    public Map<Integer, CacheNode> getCache() {
        return Collections.unmodifiableMap(cache);
    }

    public Map<Integer, Integer> getFrequencyCounter() {
        return Collections.unmodifiableMap(frequencyCounter);
    }

    public SortedMap<Integer, DoublyLinkedList> getFrequencyLru() {
        return Collections.unmodifiableSortedMap(frequencyLru);
    }

    public int capacity() {
        return capacity;
    }

    @Override
    public CacheType type() {
        return CacheType.LFU;
    }

    @Override
    public void debug() {
        System.out.println("-----");
        System.out.println("Cache contents");
        cache.forEach((key, cacheNode) -> System.out.printf("{key: %s, value: %s} ", key, cacheNode.value));
        System.out.println();
        System.out.println("-----");

        System.out.println("-----");
        System.out.println("Frequency counter");
        frequencyCounter.forEach((key, countValue) -> System.out.printf("{key: %s, counter: %s} ", key, countValue));
        System.out.println();
        System.out.println("-----");

        System.out.println("-----");
        System.out.println("Frequency LRU mapping");
        frequencyLru.forEach((frequencyCount, dll) -> {
            System.out.printf("{frequencyCount: %s, LRU list contents: \\n} ", frequencyCount);
            dll.debug();
        });
        System.out.println();
        System.out.println("-----");
    }
}
