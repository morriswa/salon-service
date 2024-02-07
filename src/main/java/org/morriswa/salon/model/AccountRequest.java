package org.morriswa.salon.model;

public record AccountRequest(
    Long userId,
    String username, 
    String password, 
    String currentPassword, 
    String confirmPassword,
    String role
) { }
