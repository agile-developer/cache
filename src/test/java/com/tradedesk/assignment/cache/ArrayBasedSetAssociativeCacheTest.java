package com.tradedesk.assignment.cache;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ArrayBasedSetAssociativeCacheTest {

    @Test
    void test_least_recently_used_cache() {

        var lruImpl = new ArrayBasedSetAssociativeCache(2, 3, CacheType.LRU);
        var value = lruImpl.get(1);
        assertThat(value).isEqualTo(-1);

        lruImpl.put(1, 1);
        lruImpl.put(2, 2);
        lruImpl.put(3, 3);
        lruImpl.put(4, 4);
        lruImpl.put(5, 5);
        lruImpl.put(6, 6);
    }
}
