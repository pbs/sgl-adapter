package org.pbs.sgladapter.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class SGLPayloadTest {
    private static final Logger logger = LoggerFactory.getLogger(SGLPayloadTest.class);
    private static Validator validator;

    private static SGLPayload.SGLPayloadBuilder buildBaseSGLFilePayloadBuilder() {
        return SGLPayload.builder()
                .caller("123e4567-e89b-12d3-a456-9AC7CBDCEE52")
                .displayName("P123123-001")
                .priority(1)
                .files(new SGLFilesPayload("P123123-001",
                        "\\\\m-isilonsmb\\gpop_dev\\mxf",
                        "P123123-001.mxf"));
    }

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    /**
     * Tests that providing a valid SGLPayload does not result in a validation error.
     */
    @Test
    public void testValidFileRestoreTaskDetails() {
        SGLPayload sglPayload = buildBaseSGLFilePayloadBuilder().build();
        Set<ConstraintViolation<SGLPayload>> violations = validator.validate(sglPayload);
        assertThat(violations).hasSize(0);
    }

    /**
     * Tests that providing an empty caller and results in a validation error.
     */
    @Test
    public void testInvalidCaller() {
        String invalidCaller = "";
        SGLPayload sglPayload = buildBaseSGLFilePayloadBuilder().caller(invalidCaller).build();

        Set<ConstraintViolation<SGLPayload>> violations = validator.validate(sglPayload);
        assertThat(violations).hasSize(1);
        violations.stream().forEach(x -> logger.info(x.getMessage()));
    }


    /**
     * Tests that providing an empty display name and it does not result in a validation error.
     */
    @Test
    public void tesValidEmptyDisplayName() {
        String emptyDisplayName = "";
        SGLPayload sglPayload = buildBaseSGLFilePayloadBuilder().displayName(emptyDisplayName).build();

        Set<ConstraintViolation<SGLPayload>> violations = validator.validate(sglPayload);
        assertThat(violations).hasSize(0);
    }

    /**
     * Tests that providing invalid priority and results in a validation error.
     */
    @Test
    public void testInvalidPriority() {
        Integer invalidPriority = 6;
        SGLPayload sglPayload = buildBaseSGLFilePayloadBuilder().priority(invalidPriority).build();

        Set<ConstraintViolation<SGLPayload>> violations = validator.validate(sglPayload);
        assertThat(violations).hasSize(1);
        violations.stream().forEach(x -> logger.info(x.getMessage()));
    }


    /**
     * Tests that providing invalid files and results in a validation error.
     */
    @Test
    public void testInvalidFiles() {
        SGLFilesPayload invalidFiles = null;
        SGLPayload sglPayload = buildBaseSGLFilePayloadBuilder().files(invalidFiles).build();

        Set<ConstraintViolation<SGLPayload>> violations = validator.validate(sglPayload);
        assertThat(violations).hasSize(1);
        violations.stream().forEach(x -> logger.info(x.getMessage()));
    }
}

