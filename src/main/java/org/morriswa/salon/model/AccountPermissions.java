package org.morriswa.salon.model;

public record AccountPermissions(
        boolean user,
        boolean client,
        boolean employee,
        boolean admin
) { }
