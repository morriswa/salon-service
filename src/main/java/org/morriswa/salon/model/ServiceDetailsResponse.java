package org.morriswa.salon.model;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

import java.net.URL;
import java.util.List;

public record ServiceDetailsResponse(
        @JsonUnwrapped ServiceDetails service,
        List<URL> content
) { }
