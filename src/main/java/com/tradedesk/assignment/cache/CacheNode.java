package com.tradedesk.assignment.cache;

import java.util.Objects;

public class CacheNode {

    final int key;
    final int value;
    CacheNode previous;
    CacheNode next;

    public CacheNode(int key, int value, CacheNode previous, CacheNode next) {
        this.key = key;
        this.value = value;
        this.previous = previous;
        this.next = next;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CacheNode cacheNode = (CacheNode) o;
        return key == cacheNode.key && value == cacheNode.value && Objects.equals(previous, cacheNode.previous) && Objects.equals(next, cacheNode.next);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, value, previous, next);
    }
}
