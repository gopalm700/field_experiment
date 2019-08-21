package com.atfarm.field.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@EqualsAndHashCode
public class FieldConditionStatistic {

    @JsonIgnore
    private long count;
    @JsonIgnore
    private double sum;
    private double min;
    private double max;

    public double getAvg() {
        return getCount() > 0 ? getSum() / getCount() : Double.NaN;
    }

    public static FieldConditionStatistic ZERO = FieldConditionStatistic.builder()
        .count(0)
        .count(0)
        .sum(0.0)
        .max(Double.NaN)
        .min(Double.NaN)
        .build();

    public FieldConditionStatistic add(double amount) {
        return plus(FieldConditionStatistic.builder()
            .count(1)
            .sum(amount)
            .min(amount)
            .max(amount)
            .build());
    }

    public FieldConditionStatistic plus(FieldConditionStatistic obj) {
        if (this.equals(ZERO)) {
            return obj;
        }
        if (obj.equals(ZERO)) {
            return obj;
        }
        return FieldConditionStatistic.builder()
            .count(this.getCount() + obj.getCount())
            .sum(this.getSum() + obj.getSum())
            .min(Math.min(this.getMin(), obj.getMin()))
            .max(Math.max(this.getMax(), obj.getMax()))
            .build();
    }
}
