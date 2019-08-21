package com.atfarm.field.storage.impl;

import com.atfarm.field.exception.InvalidTimeException;
import com.atfarm.field.storage.DataStrorage;
import java.time.LocalDateTime;
import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.SECONDS;
import java.time.temporal.TemporalUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.function.BinaryOperator;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class InMemoryStorage<T> implements DataStrorage<T> {

    private final Supplier<LocalDateTime> now;
    private final Supplier<T> factory;


    private final AtomicReferenceArray<Slot<T>> store;


    public static <T> DataStrorage<T> pastThirtyDays(final Supplier<T> factory) {
        return new InMemoryStorage<>( factory, LocalDateTime::now);
    }


    public InMemoryStorage(
        final Supplier<T> factory,
        final Supplier<LocalDateTime> now
    ) {
        this.now = now;
        this.factory = factory;
        this.store = new AtomicReferenceArray<>(366);
    }


    @Override
    public void put(LocalDateTime time, UnaryOperator<T> updater) {
        getSlot(time).update(updater);
    }


    @Override
    public T collate(BinaryOperator<T> reducer) {
        return getSlotStream().reduce(factory.get(), reducer);
    }


    protected Slot<T> getSlot(LocalDateTime time) {
        final int index = checkSlotIndex(time);
        final int offset = offset(index);
        return store.updateAndGet(offset, value -> actual(time, value));
    }


    protected Stream<T> getSlotStream() {
        final LocalDateTime now = this.now.get();

        final int firstIndex = minimalIndexFor(now);
        final int lastIndex = currentIndexFor(now);

        return IntStream.rangeClosed(firstIndex, lastIndex)
            .mapToObj(index -> historical(index, store.get(offset(index))))
            .filter(Objects::nonNull)
            .map(Slot::getValue);
    }


    private Slot<T> historical(final int index, final Slot<T> slot) {
        return slot != null && slot.getDate().getDayOfYear() == index ? slot : null;
    }


    private Slot<T> actual(LocalDateTime time, final Slot<T> value) {
        return value == null || value.getDate().toLocalDate().isBefore(time.toLocalDate()) ? new Slot<>(time, factory.get()) : value;
    }


    private int currentIndexFor(final LocalDateTime time) {
        return time.getDayOfYear();
    }


    private int minimalIndexFor(final LocalDateTime time) {
        return time.minus(30, DAYS).getDayOfYear();
    }


    private int offset(final long index) {
        return (int) (index % store.length());
    }


    private int checkSlotIndex(final LocalDateTime time) {
        LocalDateTime now = this.now.get();

        LocalDateTime test = now.minus(30, DAYS);
        if (now.minus(30, DAYS).isAfter(time)) {
            throw new InvalidTimeException("Not require to store old time");
        }
        if (time.isAfter(now)) {
            throw new InvalidTimeException("Future time is not reqd.");
        }
        return currentIndexFor(time);
    }


    protected static class Slot<E> {
        private final LocalDateTime date;
        private final AtomicReference<E> value;


        public Slot(LocalDateTime date, E value) {
            this.date = date;
            this.value = new AtomicReference<>(value);
        }


        public void update(UnaryOperator<E> updater) {
            value.updateAndGet(updater);
        }


        public E getValue() {
            return value.get();
        }


        public LocalDateTime getDate() {
            return date;
        }
    }
}
