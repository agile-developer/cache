package com.tradedesk.assignment.cache;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class LeastFrequentlyUsedCacheTest {

    @Test
    void inserting_within_capacity_works_as_expected() {

        var lfuCache = new LeastFrequentlyUsedCache(3);
        lfuCache.put(1, 1);
        lfuCache.put(2, 2);
        lfuCache.put(3, 3);

        assertThat(lfuCache.getCache().size()).isEqualTo(3);
        assertThat(lfuCache.getFrequencyLru().firstKey()).isEqualTo(1);
        var lruList = lfuCache.getFrequencyLru().get(1);
        assertThat(lruList.size()).isEqualTo(3);
        assertThat(lruList.leastRecentlyUsed.next.key).isEqualTo(1);
        assertThat(lruList.mostRecentlyUsed.previous.key).isEqualTo(3);
    }

    @Test
    void inserting_and_getting_within_capacity_works_as_expected() {

        var lfuCache = new LeastFrequentlyUsedCache(3);
        lfuCache.put(1, 1);
        lfuCache.put(2, 2);
        var value = lfuCache.get(3);
        assertThat(value).isEqualTo(-1);
        lfuCache.put(3, 3);
        value = lfuCache.get(1);
        assertThat(value).isEqualTo(1);

        assertThat(lfuCache.getCache().size()).isEqualTo(3);
        assertThat(lfuCache.getFrequencyLru().firstKey()).isEqualTo(1);
        assertThat(lfuCache.getFrequencyLru().lastKey()).isEqualTo(2);
        var lruList1 = lfuCache.getFrequencyLru().get(1);
        assertThat(lruList1.size()).isEqualTo(2);
        assertThat(lruList1.leastRecentlyUsed.next.key).isEqualTo(2);
        assertThat(lruList1.mostRecentlyUsed.previous.key).isEqualTo(3);

        var lruList2 = lfuCache.getFrequencyLru().get(2);
        assertThat(lruList2.size()).isEqualTo(1);
        assertThat(lruList2.leastRecentlyUsed.next.key).isEqualTo(1);
    }

    @Test
    void inserting_exceeding_capacity_works_as_expected() {

        var lfuCache = new LeastFrequentlyUsedCache(3);
        lfuCache.put(1, 1);
        lfuCache.put(2, 2);
        lfuCache.put(3, 3);
        lfuCache.put(4, 4);
        lfuCache.put(5, 5);
        lfuCache.put(6, 6);

        assertThat(lfuCache.getCache().size()).isEqualTo(3);
        assertThat(lfuCache.getFrequencyCounter().size()).isEqualTo(3);
        assertThat(lfuCache.getFrequencyLru().firstKey()).isEqualTo(1);
        var lruList = lfuCache.getFrequencyLru().get(1);
        assertThat(lruList.size()).isEqualTo(3);
        assertThat(lruList.leastRecentlyUsed.next.key).isEqualTo(4);
        assertThat(lruList.mostRecentlyUsed.previous.key).isEqualTo(6);
    }

    @Test
    void inserting_and_getting_exceeding_capacity_works_as_expected() {

        var lfuCache = new LeastFrequentlyUsedCache(3);
        lfuCache.put(1, 1);
        lfuCache.put(2, 2);
        var value = lfuCache.get(3);
        assertThat(value).isEqualTo(-1);
        lfuCache.put(3, 3);
        value = lfuCache.get(1);
        assertThat(value).isEqualTo(1);
        lfuCache.put(4, 4);

        var frequencyCounter = lfuCache.getFrequencyCounter();
        assertThat(frequencyCounter.size()).isEqualTo(3);
        assertThat(frequencyCounter.get(1)).isEqualTo(2);
        assertThat(frequencyCounter.get(2)).isNull();
        assertThat(frequencyCounter.get(3)).isEqualTo(1);
        assertThat(frequencyCounter.get(4)).isEqualTo(1);

        assertThat(lfuCache.getCache().size()).isEqualTo(3);
        assertThat(lfuCache.getCache().get(2)).isNull();
        assertThat(lfuCache.getFrequencyLru().firstKey()).isEqualTo(1);
        var lruList1 = lfuCache.getFrequencyLru().get(1);
        assertThat(lruList1.size()).isEqualTo(2);
        assertThat(lruList1.leastRecentlyUsed.next.key).isEqualTo(3);
        assertThat(lruList1.mostRecentlyUsed.previous.key).isEqualTo(4);

        assertThat(lfuCache.getFrequencyLru().lastKey()).isEqualTo(2);
        var lruList2 = lfuCache.getFrequencyLru().get(2);
        assertThat(lruList2.size()).isEqualTo(1);
        assertThat(lruList2.leastRecentlyUsed.next.key).isEqualTo(1);
    }

    @Test
    void inserting_and_updating_within_capacity_works_as_expected() {

        var lfuCache = new LeastFrequentlyUsedCache(3);
        lfuCache.put(1, 1);
        lfuCache.put(2, 2);
        lfuCache.put(3, 3);
        lfuCache.put(1, 10);
        lfuCache.put(3, 30);

        assertThat(lfuCache.getCache().size()).isEqualTo(3);
        assertThat(lfuCache.getFrequencyLru().firstKey()).isEqualTo(1);
        var lruList = lfuCache.getFrequencyLru().get(1);
        assertThat(lruList.size()).isEqualTo(1);
        assertThat(lruList.leastRecentlyUsed.next.key).isEqualTo(2);

        assertThat(lfuCache.getFrequencyLru().lastKey()).isEqualTo(2);
        var lruList2 = lfuCache.getFrequencyLru().get(2);
        assertThat(lruList2.size()).isEqualTo(2);
        assertThat(lruList2.leastRecentlyUsed.next.key).isEqualTo(1);
        assertThat(lruList2.mostRecentlyUsed.previous.key).isEqualTo(3);
    }

    @Test
    void inserting_and_updating_exceeding_capacity_works_as_expected() {

        var lfuCache = new LeastFrequentlyUsedCache(3);
        lfuCache.put(1, 1);
        lfuCache.put(2, 2);
        lfuCache.put(3, 3);
        lfuCache.put(1, 10);
        lfuCache.put(3, 30);
        lfuCache.put(4, 4);
        lfuCache.put(4, 40);
        lfuCache.put(5, 5);

        assertThat(lfuCache.getCache().size()).isEqualTo(3);
        assertThat(lfuCache.getFrequencyLru().firstKey()).isEqualTo(1);
        var lruList = lfuCache.getFrequencyLru().get(1);
        assertThat(lruList.size()).isEqualTo(1);
        assertThat(lruList.leastRecentlyUsed.next.key).isEqualTo(5);

        assertThat(lfuCache.getFrequencyLru().lastKey()).isEqualTo(2);
        var lruList2 = lfuCache.getFrequencyLru().get(2);
        assertThat(lruList2.size()).isEqualTo(2);
        assertThat(lruList2.leastRecentlyUsed.next.key).isEqualTo(3);
        assertThat(lruList2.mostRecentlyUsed.previous.key).isEqualTo(4);
    }

    @Test
    void inserting_updating_and_getting_work_as_expected() {

        var lfuCache = new LeastFrequentlyUsedCache(3);
        lfuCache.put(1, 1);
        lfuCache.put(2, 2);
        lfuCache.put(3, 3);

        var value = lfuCache.get(1);
        assertThat(value).isEqualTo(1);
        assertThat(lfuCache.getFrequencyLru().size()).isEqualTo(2);

        value = lfuCache.get(2);
        assertThat(value).isEqualTo(2);
        assertThat(lfuCache.getFrequencyLru().size()).isEqualTo(2);
        assertThat(lfuCache.getFrequencyLru().lastKey()).isEqualTo(2);

        value = lfuCache.get(4);
        assertThat(value).isEqualTo(-1);

        lfuCache.put(4, 4);
        assertThat(lfuCache.getFrequencyLru().size()).isEqualTo(2);
        assertThat(lfuCache.getFrequencyLru().firstKey()).isEqualTo(1);
        assertThat(lfuCache.getFrequencyLru().lastKey()).isEqualTo(2);
        var lruList1 = lfuCache.getFrequencyLru().get(1);
        assertThat(lruList1.size()).isEqualTo(1);
        assertThat(lruList1.mostRecentlyUsed.previous.value).isEqualTo(4);
        var lruList2 = lfuCache.getFrequencyLru().get(2);
        assertThat(lruList2.size()).isEqualTo(2);
        assertThat(lruList2.leastRecentlyUsed.next.key).isEqualTo(1);
        assertThat(lruList2.mostRecentlyUsed.previous.key).isEqualTo(2);

        lfuCache.put(2, 20);
        assertThat(lfuCache.getFrequencyLru().size()).isEqualTo(3);
        assertThat(lfuCache.getFrequencyLru().firstKey()).isEqualTo(1);
        assertThat(lfuCache.getFrequencyLru().lastKey()).isEqualTo(3);

        lruList1 = lfuCache.getFrequencyLru().get(1);
        assertThat(lruList1.size()).isEqualTo(1);
        assertThat(lruList1.mostRecentlyUsed.previous.value).isEqualTo(4);
        lruList2 = lfuCache.getFrequencyLru().get(2);
        assertThat(lruList2.size()).isEqualTo(1);
        assertThat(lruList2.mostRecentlyUsed.previous.value).isEqualTo(1);
        var lruList3 = lfuCache.getFrequencyLru().get(3);
        assertThat(lruList3.size()).isEqualTo(1);
        assertThat(lruList3.mostRecentlyUsed.previous.value).isEqualTo(20);
    }
}
