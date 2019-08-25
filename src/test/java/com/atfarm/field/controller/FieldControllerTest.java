package com.atfarm.field.controller;

import com.atfarm.field.controller.dto.FieldCondition;
import com.atfarm.field.controller.dto.StatisticsDto;
import com.atfarm.field.domain.FieldConditionStatistic;
import com.atfarm.field.exception.InvalidTimeException;
import com.atfarm.field.service.FieldService;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(FieldController.class)
public class FieldControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private FieldService fieldService;


    @Test
    public void shouldCreateRecord() throws Exception {
        doNothing().when(fieldService).add(any(FieldCondition.class));
        mvc.perform(post("/api/field-conditions")
            .contentType("application/json")
            .content("{ \"vegetation\": 0.82, \"occurrenceAt\": \"2019-04-23T08:50Z\"}"))
            .andExpect(status().isCreated());

        verify(fieldService).add(any(FieldCondition.class));
    }


    @Test
    public void testInvalidTimeStamp() throws Exception {
        doThrow(new InvalidTimeException(""))
            .when(fieldService).add(any(FieldCondition.class));

        mvc.perform(post("/api/field-conditions")
            .contentType("application/json")
            .content("{\"vegetation\": 10.0,\"occurrenceAt\": \"2019-04-23T08:50Z\"}"))
            .andExpect(status().isAccepted());
    }


    @Test
    public void testStatistics() throws Exception {
        FieldConditionStatistic stat = FieldConditionStatistic.builder()
            .count(2)
            .sum(15.0)
            .max(10.0)
            .min(5.5)
            .build();
        StatisticsDto dto = new StatisticsDto(stat);
        when(fieldService.getStat()).thenReturn(stat);

        MvcResult result = mvc.perform(get("/api/field-statistics").accept("application/json"))
            .andExpect(status().isOk())
            .andReturn();
        ObjectMapper mapper = new ObjectMapper();
        assertThat(result.getResponse().getContentAsString()).isEqualToIgnoringWhitespace(mapper.writeValueAsString(dto));
    }

}
