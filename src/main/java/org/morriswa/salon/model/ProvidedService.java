package org.morriswa.salon.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@AllArgsConstructor @Getter
public class ProvidedService {
    private final Long serviceId;
    private final BigDecimal cost;
    private final Integer length;
    private final String name;
}