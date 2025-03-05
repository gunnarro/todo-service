package org.gunnarro.microservice.todoservice.domain;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.format.DateTimeFormatterBuilder;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OderProcessorTest {

    @Test
    void nextState() {
        OrderProcessor.OrderState ny = OrderProcessor.OrderState.NY;
        assertEquals(OrderProcessor.OrderState.VAREOVERFORING, ny.nextState());
    }

    @Test
    void isProcessed() throws JsonProcessingException {
        OrdreStatestikk ordreStatestikk = new OrdreStatestikk();
        ordreStatestikk.setOrdreType("ALM");
        ordreStatestikk.setAntall(57);
        ordreStatestikk.setFraDato(LocalDate.of(2025, 1,1));
        ordreStatestikk.setTilDato(LocalDate.of(2025, 1,31));
        Map<String, Integer> stageMap = new HashMap<>();
        stageMap.put("NY", 34);
        stageMap.put("KLAR", 34);
        stageMap.put("VAREOVERFØRING", 34);
        stageMap.put("FAKTURERING", 4);
        stageMap.put("VAREUTMELDING", 12);
        stageMap.put("KONTROLL_OG_PAKK", 6);
        stageMap.put("TRANSPORT", 9);
        stageMap.put("UTLEVERING", 2);
        ordreStatestikk.setStageMap(stageMap);

        ObjectMapper mapper = JsonMapper.builder()
                .build()
                .registerModule(new JavaTimeModule());

        System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(ordreStatestikk));
    }

    class OrdreStatestikk {
        LocalDate fraDato;
        LocalDate tilDato;
        String ordreType;
        Integer antall;
        Map<String, Integer> stageMap;

        public OrdreStatestikk() {}

        public LocalDate getFraDato() {
            return fraDato;
        }

        public void setFraDato(LocalDate fraDato) {
            this.fraDato = fraDato;
        }

        public LocalDate getTilDato() {
            return tilDato;
        }

        public void setTilDato(LocalDate tilDato) {
            this.tilDato = tilDato;
        }

        public String getOrdreType() {
            return ordreType;
        }

        public void setOrdreType(String ordreType) {
            this.ordreType = ordreType;
        }

        public Integer getAntall() {
            return antall;
        }

        public void setAntall(Integer antall) {
            this.antall = antall;
        }

        public Map<String, Integer> getStageMap() {
            return stageMap;
        }

        public void setStageMap(Map<String, Integer> stageMap) {
            this.stageMap = stageMap;
        }
    }
}
