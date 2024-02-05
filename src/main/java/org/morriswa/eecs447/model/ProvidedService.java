package org.morriswa.eecs447.model;

import java.math.BigDecimal;

public record ProvidedService(
    Long serviceId,
    BigDecimal defaultCost,
    String name
) { } 