package com.api.kafkavotacao.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@Data
public class ResultPayload {

    @JsonProperty(value = "pauta")
    private String pauta;

    @JsonProperty(value = "resultado")
    private Map<String,Long> resultado;
}
