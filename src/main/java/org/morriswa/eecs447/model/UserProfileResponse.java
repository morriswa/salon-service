package org.morriswa.eecs447.model;

import java.time.ZonedDateTime;

public record UserProfileResponse(Long userId, String username, ZonedDateTime dateCreated)
{ }
