package com.atfarm.field.storage;

import java.time.LocalDateTime;
import java.util.function.BinaryOperator;
import java.util.function.UnaryOperator;

public interface DataStrorage<T> {

    void put(LocalDateTime timestamp, UnaryOperator<T> operator);

    T collate(BinaryOperator<T> operator);
}
