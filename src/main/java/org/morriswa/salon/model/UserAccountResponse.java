package org.morriswa.salon.model;

import java.time.ZonedDateTime;
import java.util.Set;

public record UserAccountResponse(
        Long userId,
        String username,
        ZonedDateTime accountCreationDate,
        Set<String> authorities
) { }
