package org.morriswa.salon.model;

import lombok.Getter;

import java.net.URL;
import java.util.List;

@Getter
public class ProvidedServiceProfile extends ProvidedServiceDetails {
    private final List<URL> content;

    public ProvidedServiceProfile(ProvidedServiceDetails details, List<URL> content) {
        super(  details.getServiceId(), details.getCost(), details.getLength(), details.getName(),
                details.getEmployee());
        this.content = content;
    }
}
