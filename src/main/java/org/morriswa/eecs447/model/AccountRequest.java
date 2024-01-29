package org.morriswa.eecs447.model;

public record AccountRequest(String username, String password, String currentPassword, String confirmPassword)
{ }
