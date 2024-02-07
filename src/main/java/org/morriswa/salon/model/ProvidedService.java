package org.morriswa.salon.model;

import java.math.BigDecimal;

public record ProvidedService(
    Long serviceId,
    BigDecimal defaultCost,
    String name
) { } 