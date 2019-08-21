package com.atfarm.field.controller;


import com.atfarm.field.controller.dto.StatisticsDto;
import com.atfarm.field.controller.dto.FieldCondition;
import com.atfarm.field.service.FieldService;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class FieldController {

    private final static Logger logger = LoggerFactory.getLogger(FieldController.class);

    @Autowired
    private FieldService service;

    @PostMapping(path="/field-conditions",consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public void addValue(@RequestBody @Valid FieldCondition condition){
        logger.debug("adding data ");
        service.add(condition);
    }

    @GetMapping(path = "/field-statistics", produces = "application/json")
    public StatisticsDto getStats(){
        return new StatisticsDto(service.getStat());
    }

}
