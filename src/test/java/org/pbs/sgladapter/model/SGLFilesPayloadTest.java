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

public class SGLFilesPayloadTest {
    private static final Logger logger = LoggerFactory.getLogger(SGLFilesPayloadTest.class);
    private static Validator validator;

    private static SGLFilesPayload.SGLFilesPayloadBuilder buildBaseSGLFilePayloadBuilder() {
        return SGLFilesPayload.builder()
                .path("\\\\m-isilonsmb\\gpop_dev\\mxf")
                .guid("P123123-001")
                .filename("P123123-001.mxf");
    }

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    /**
     * Tests that providing a valid SGLFilesPayload does not result in a validation error.
     */
    @Test
    public void testValidFileRestoreTaskDetails() {
        SGLFilesPayload sglFilesPayload = buildBaseSGLFilePayloadBuilder().build();
        Set<ConstraintViolation<SGLFilesPayload>> violations = validator.validate(sglFilesPayload);
        assertThat(violations).hasSize(0);
    }

    /**
     * Tests that providing an empty path and results in a validation error.
     */
    @Test
    public void testInvalidPath() {
        String invalidPath = "";
        SGLFilesPayload sglFilesPayload = buildBaseSGLFilePayloadBuilder().path(invalidPath).build();

        Set<ConstraintViolation<SGLFilesPayload>> violations = validator.validate(sglFilesPayload);
        assertThat(violations).hasSize(1);
        violations.stream().forEach(x -> logger.info(x.getMessage()));
    }


    /**
     * Tests that providing an empty filename and results in a validation error.
     */
    @Test
    public void tesValidEmptyFilename() {
        String invalidFilename = "";
        SGLFilesPayload sglFilesPayload = buildBaseSGLFilePayloadBuilder().filename(invalidFilename).build();

        Set<ConstraintViolation<SGLFilesPayload>> violations = validator.validate(sglFilesPayload);
        assertThat(violations).hasSize(0);
    }

    /**
     * Tests that providing an empty resourceId and results in a validation error.
     */
    @Test
    public void testInvalidGuid() {
        String invalidFilename = "";
        SGLFilesPayload sglFilesPayload = buildBaseSGLFilePayloadBuilder().guid(invalidFilename).build();

        Set<ConstraintViolation<SGLFilesPayload>> violations = validator.validate(sglFilesPayload);
        assertThat(violations).hasSize(1);
        violations.stream().forEach(x -> logger.info(x.getMessage()));
    }
}

