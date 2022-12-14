package org.pbs.sgladapter.sample;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PropertySourceResolver {

    @Value("${example.firstProperty}")
    private String firstProperty;
    @Value("${example.secondProperty}")
    private String secondProperty;

    public String getFirstProperty() {
        return firstProperty;
    }

    public String getSecondProperty() {
        return secondProperty;
    }
}
