package org.pbs.sgladapter.sample;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class TestResourcePropertySourceResolverIntegrationTest {

    @Autowired
    private PropertySourceResolver propertySourceResolver;

    @Test
    public void shouldTestResourceFile_overridePropertyValues() {
        String firstProperty = propertySourceResolver.getFirstProperty();
        String secondProperty = propertySourceResolver.getSecondProperty();

        assertEquals("file", firstProperty);
        assertEquals("file", secondProperty);
    }
}
