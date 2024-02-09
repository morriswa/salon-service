package org.morriswa.salon.model;

import java.math.BigDecimal;

public record ProvidedService(
    Long serviceId,
    BigDecimal defaultCost,
    Integer defaultLength,
    String name
) { } 