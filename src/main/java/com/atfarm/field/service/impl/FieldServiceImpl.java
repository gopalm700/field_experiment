package com.atfarm.field.service.impl;

import com.atfarm.field.controller.dto.FieldCondition;
import com.atfarm.field.domain.FieldConditionStatistic;
import com.atfarm.field.service.FieldService;
import com.atfarm.field.storage.DataStrorage;
import com.atfarm.field.storage.impl.InMemoryStorage;
import java.time.LocalDateTime;
import java.time.ZoneId;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FieldServiceImpl implements FieldService {

    private DataStrorage<FieldConditionStatistic> storage;


    @Autowired
    public FieldServiceImpl() {
        this(InMemoryStorage.pastThirtyDays(() -> FieldConditionStatistic.ZERO));
    }


    @Override
    public void add(FieldCondition condition) {
        LocalDateTime time = condition.getOccurrenceAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        storage.put(time, s -> s.add(condition.getVegetation()));
    }


    @Override
    public FieldConditionStatistic getStat() {
        return storage.collate(FieldConditionStatistic::plus);
    }

}

