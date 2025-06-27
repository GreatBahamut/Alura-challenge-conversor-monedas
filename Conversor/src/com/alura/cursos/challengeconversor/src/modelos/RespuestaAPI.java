package com.alura.cursos.challengeconversor.src.modelos;

import java.util.Map;

public class RespuestaAPI {
    private Map<String, Double> conversion_rates;

    public double getUSD() {
        return conversion_rates.getOrDefault("USD", -1.0);
    }
}

