package org.pbs.sgladapter.sample;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
public class ProfilePropertySourceResolverIntegrationTest {

    @Autowired
    private PropertySourceResolver propertySourceResolver;

    @Test
    public void shouldProfiledProperty_overridePropertyValues() {
        String firstProperty = propertySourceResolver.getFirstProperty();
        String secondProperty = propertySourceResolver.getSecondProperty();

        assertEquals("profile", firstProperty);
        assertEquals("file", secondProperty);
    }
}
