package com.atfarm.field.storage.impl;

import com.atfarm.field.domain.FieldConditionStatistic;
import com.atfarm.field.exception.InvalidTimeException;
import java.time.LocalDateTime;
import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Before;
import org.junit.Test;

public class InMemoryStorageTest {

    private static final LocalDateTime NOW = LocalDateTime.now();

    private InMemoryStorage<FieldConditionStatistic> memory;


    @Before
    public void init() {
        memory = new InMemoryStorage<>(() -> FieldConditionStatistic.ZERO, () -> NOW);
    }


    @Test(expected = InvalidTimeException.class)
    public void shouldGetExceptionForOldValue() {
        memory.put(NOW.minus(30, DAYS).minus(1, SECONDS), e -> e.add(10.0));
    }


    @Test(expected = InvalidTimeException.class)
    public void shouldGetExceptionForFutureValue() {
        memory.put(NOW.plus(5, SECONDS), e -> e.add(5.0));
    }


    @Test
    public void testSlotComputationForOneValue() {
        memory.put(NOW, s -> s.add(12.0));

        final FieldConditionStatistic value = memory.getSlot(NOW).getValue();
        assertThat(value).isEqualTo(FieldConditionStatistic.builder().count(1).max(12.0).min(12.0).sum(12.0).build());
    }


    @Test
    public void testSlotComputationultipleValue() {
        memory.put(NOW, s -> s.add(10.0));
        memory.put(NOW, s -> s.add(20.0));
        memory.put(NOW, s -> s.add(30.0));
        memory.put(NOW, s -> s.add(47.0));
        final FieldConditionStatistic value = memory.getSlot(NOW).getValue();

        assertThat(value).isEqualTo(FieldConditionStatistic.builder().count(4).max(47.0).min(10.0).sum(107.0).build());

    }


    @Test
    public void testEmptyValues() {
        final FieldConditionStatistic value = memory.collate(FieldConditionStatistic::plus);
        assertThat(value).isEqualTo(FieldConditionStatistic.ZERO);

    }


    @Test
    public void testsingleReducedValue() {
        memory.put(NOW, e -> e.add(100.0));
        final FieldConditionStatistic value = memory.collate(FieldConditionStatistic::plus);
        assertThat(value).isEqualTo(FieldConditionStatistic.builder().count(1).max(100.0).min(100.0).sum(100.0).build());
    }


    @Test
    public void testMultipleValuesinMultipleTime() {
        memory.put(NOW.minus(30, DAYS), e -> e.add(30.0));
        memory.put(NOW.minus(5, SECONDS), e -> e.add(40.0));
        memory.put(NOW.minus(10, SECONDS), e -> e.add(40.0));
        memory.put(NOW, e -> e.add(50.0));

        final FieldConditionStatistic value = memory.collate(FieldConditionStatistic::plus);
        assertThat(value).isEqualTo(FieldConditionStatistic.builder().count(4).max(50.0).min(30.0).sum(160.0).build());
    }


    @Test
    public void testMultipleValuesInSameTime() {
        memory.put(NOW, e -> e.add(15.0));
        memory.put(NOW, e -> e.add(20.0));
        memory.put(NOW, e -> e.add(30.0));

        final FieldConditionStatistic value = memory.collate(FieldConditionStatistic::plus);
        assertThat(value).isEqualTo(FieldConditionStatistic.builder().count(3).max(30.0).min(15.0).sum(65.0).build());

    }
}
