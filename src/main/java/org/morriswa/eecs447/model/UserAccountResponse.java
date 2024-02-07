package org.morriswa.eecs447.model;

import java.time.ZonedDateTime;
import java.util.Set;

public record UserAccountResponse(
        Long userId,
        String username,
        ZonedDateTime accountCreationDate,
        Set<String> authorities
) { }
