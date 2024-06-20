package com.tradedesk.assignment.cache;

public class DoublyLinkedList {

    final CacheNode leastRecentlyUsed;
    final CacheNode mostRecentlyUsed;
    private int size;

    public DoublyLinkedList() {
        this.leastRecentlyUsed = new CacheNode(-1, -1, null, null);
        this.mostRecentlyUsed = new CacheNode(-1, -1, null, null);
        this.leastRecentlyUsed.next = this.mostRecentlyUsed;
        this.mostRecentlyUsed.previous = this.leastRecentlyUsed;
    }

    public void insertAsMostRecentlyUsed(CacheNode node) {

        var lastMru = mostRecentlyUsed.previous;
        mostRecentlyUsed.previous = node;
        node.previous = lastMru;
        node.next = mostRecentlyUsed;
        lastMru.next = node;
        size++;
    }

    public void removeFromCurrentPosition(CacheNode node) {

        var removedNodesPrevious = node.previous;
        var removedNodesNext = node.next;
        removedNodesPrevious.next = removedNodesNext;
        removedNodesNext.previous = removedNodesPrevious;
        size--;
    }

    public boolean isEmpty() {

        return this.leastRecentlyUsed.next == mostRecentlyUsed;
    }

    public int size() {

        return size;
    }

    void debug() {

        var next = leastRecentlyUsed.next;
        System.out.println("-----");
        while (next != mostRecentlyUsed) {
            System.out.printf("{ key: %s, value: %s } -> ", next.key, next.value);
            next = next.next;
        }
        System.out.println();
        System.out.println("-----");
    }
}
