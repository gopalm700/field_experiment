package com.atfarm.field.service;

import com.atfarm.field.controller.dto.FieldCondition;
import com.atfarm.field.domain.FieldConditionStatistic;

public interface FieldService {

    public void add(final FieldCondition transaction);

    public FieldConditionStatistic getStat();

}
