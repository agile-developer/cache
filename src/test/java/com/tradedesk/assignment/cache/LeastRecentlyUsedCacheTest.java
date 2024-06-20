package com.tradedesk.assignment.cache;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class LeastRecentlyUsedCacheTest {

    @Test
    void inserting_elements_within_capacity_causes_no_evictions() {

        var lruCache = new LeastRecentlyUsedCache(3);
        lruCache.put(1,1);
        lruCache.put(2,2);
        lruCache.put(3,3);

        assertThat(lruCache.getDoublyLinkedList().size()).isEqualTo(3);
        assertThat(lruCache.cache().size()).isEqualTo(3);
        assertThat(lruCache.getDoublyLinkedList().mostRecentlyUsed.previous.key).isEqualTo(3);
        assertThat(lruCache.getDoublyLinkedList().mostRecentlyUsed.previous.value).isEqualTo(3);
        assertThat(lruCache.getDoublyLinkedList().leastRecentlyUsed.next.key).isEqualTo(1);
        assertThat(lruCache.getDoublyLinkedList().leastRecentlyUsed.next.value).isEqualTo(1);
    }

    @Test
    void inserting_elements_exceeding_capacity_causes_evictions() {

        var lruCache = new LeastRecentlyUsedCache(3);
        lruCache.put(1,1);
        lruCache.put(2,2);
        lruCache.put(3,3);
        lruCache.put(4,4);
        lruCache.put(5,5);

        assertThat(lruCache.getDoublyLinkedList().size()).isEqualTo(3);
        assertThat(lruCache.cache().size()).isEqualTo(3);
        assertThat(lruCache.getDoublyLinkedList().mostRecentlyUsed.previous.key).isEqualTo(5);
        assertThat(lruCache.getDoublyLinkedList().mostRecentlyUsed.previous.value).isEqualTo(5);
        assertThat(lruCache.getDoublyLinkedList().leastRecentlyUsed.next.key).isEqualTo(3);
        assertThat(lruCache.getDoublyLinkedList().leastRecentlyUsed.next.value).isEqualTo(3);
    }

    @Test
    void accessing_elements_in_cache_updates_mru_and_lru_elements() {

        var lruCache = new LeastRecentlyUsedCache(3);
        lruCache.put(1,1);
        lruCache.put(2,2);
        var value = lruCache.get(3);
        assertThat(value).isEqualTo(-1);
        assertThat(lruCache.getDoublyLinkedList().size()).isEqualTo(2);
        assertThat(lruCache.cache().size()).isEqualTo(2);

        lruCache.put(3,3);
        value = lruCache.get(3);
        assertThat(value).isEqualTo(3);
        assertThat(lruCache.getDoublyLinkedList().size()).isEqualTo(3);
        assertThat(lruCache.cache().size()).isEqualTo(3);

        value = lruCache.get(1);
        assertThat(value).isEqualTo(1);
        assertThat(lruCache.getDoublyLinkedList().size()).isEqualTo(3);
        assertThat(lruCache.cache().size()).isEqualTo(3);
        assertThat(lruCache.getDoublyLinkedList().mostRecentlyUsed.previous.key).isEqualTo(1);
        assertThat(lruCache.getDoublyLinkedList().mostRecentlyUsed.previous.value).isEqualTo(1);
        assertThat(lruCache.getDoublyLinkedList().leastRecentlyUsed.next.key).isEqualTo(2);
        assertThat(lruCache.getDoublyLinkedList().leastRecentlyUsed.next.value).isEqualTo(2);

        lruCache.put(4,4);
        assertThat(lruCache.getDoublyLinkedList().size()).isEqualTo(3);
        assertThat(lruCache.cache().size()).isEqualTo(3);
        assertThat(lruCache.getDoublyLinkedList().mostRecentlyUsed.previous.key).isEqualTo(4);
        assertThat(lruCache.getDoublyLinkedList().mostRecentlyUsed.previous.value).isEqualTo(4);
        assertThat(lruCache.getDoublyLinkedList().leastRecentlyUsed.next.key).isEqualTo(3);
        assertThat(lruCache.getDoublyLinkedList().leastRecentlyUsed.next.value).isEqualTo(3);
    }

    @Test
    void updating_an_existing_key_works_as_expected() {

        var lruCache = new LeastRecentlyUsedCache(3);
        lruCache.put(1,1);
        lruCache.put(2,2);
        lruCache.put(1,10);

        assertThat(lruCache.getDoublyLinkedList().size()).isEqualTo(2);
        assertThat(lruCache.cache().size()).isEqualTo(2);
        assertThat(lruCache.getDoublyLinkedList().mostRecentlyUsed.previous.key).isEqualTo(1);
        assertThat(lruCache.getDoublyLinkedList().mostRecentlyUsed.previous.value).isEqualTo(10);
        assertThat(lruCache.getDoublyLinkedList().leastRecentlyUsed.next.key).isEqualTo(2);
        assertThat(lruCache.getDoublyLinkedList().leastRecentlyUsed.next.value).isEqualTo(2);
    }

    @Test
    void updating_an_existing_key_works_as_expected_2() {

        var lruCache = new LeastRecentlyUsedCache(3);
        lruCache.put(1,1);
        lruCache.put(2,2);
        lruCache.put(3,3);
        lruCache.put(1,10);

        assertThat(lruCache.getDoublyLinkedList().size()).isEqualTo(3);
        assertThat(lruCache.cache().size()).isEqualTo(3);
        assertThat(lruCache.getDoublyLinkedList().mostRecentlyUsed.previous.key).isEqualTo(1);
        assertThat(lruCache.getDoublyLinkedList().mostRecentlyUsed.previous.value).isEqualTo(10);
        assertThat(lruCache.getDoublyLinkedList().leastRecentlyUsed.next.key).isEqualTo(2);
        assertThat(lruCache.getDoublyLinkedList().leastRecentlyUsed.next.value).isEqualTo(2);
    }

    @Test
    void isEmpty_works_as_expected() {

        var list = new DoublyLinkedList();
        assertThat(list.isEmpty()).isTrue();

        var node1 = new CacheNode(1, 1, null, null);
        var node2 = new CacheNode(2, 2, null, null);
        var node3 = new CacheNode(3, 3, null, null);
        list.insertAsMostRecentlyUsed(node1);
        list.insertAsMostRecentlyUsed(node2);
        list.insertAsMostRecentlyUsed(node3);
        assertThat(list.isEmpty()).isFalse();
        assertThat(list.size()).isEqualTo(3);

        list.removeFromCurrentPosition(node2);
        assertThat(list.size()).isEqualTo(2);
        assertThat(list.isEmpty()).isFalse();

        list.removeFromCurrentPosition(node1);
        assertThat(list.size()).isEqualTo(1);
        assertThat(list.isEmpty()).isFalse();

        list.removeFromCurrentPosition(node3);
        assertThat(list.isEmpty()).isTrue();
    }

}
