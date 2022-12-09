package org.pbs.sgladapter.model;

import static org.assertj.core.api.Assertions.assertThat;

import lombok.experimental.SuperBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

public class FileRestoreTaskDetailsTest {
    private static final Logger logger = LoggerFactory.getLogger(FileRestoreTaskDetailsTest.class);
    private static Validator validator;

    private static FileRestoreTaskDetails.FileRestoreTaskDetailsBuilder buildBaseFileRestoreTaskDetailsBuilder() {
        return FileRestoreTaskDetails.builder()
                .path("\\\\m-isilonsmb\\gpop_dev\\mxf")
                .resourceId("P123123-001")
                .filename("P123123-001.mxf");
    }

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    /**
     * Tests that providing a valid FileRestoreTaskDetails does not result in a validation error.
     */
    @Test
    public void testValidFileRestoreTaskDetails() {
        FileRestoreTaskDetails taskDetails = buildBaseFileRestoreTaskDetailsBuilder().build();
        Set<ConstraintViolation<FileRestoreTaskDetails>> violations = validator.validate(taskDetails);
        assertThat(violations).hasSize(0);
    }

    /**
     * Tests that providing an empty path and results in a validation error.
     */
    @Test
    public void testInvalidPath() {
        String invalidPath = "";
        FileRestoreTaskDetails taskDetails = buildBaseFileRestoreTaskDetailsBuilder().path(invalidPath).build();

        Set<ConstraintViolation<FileRestoreTaskDetails>> violations = validator.validate(taskDetails);
        assertThat(violations).hasSize(1);
        violations.stream().forEach(x -> logger.info(x.getMessage()));
    }


    /**
     * Tests that providing an empty filename and results in a validation error.
     */
    @Test
    public void testInvalidFilename() {
        String invalidFilename = "";
        FileRestoreTaskDetails taskDetails = buildBaseFileRestoreTaskDetailsBuilder().filename(invalidFilename).build();

        Set<ConstraintViolation<FileRestoreTaskDetails>> violations = validator.validate(taskDetails);
        assertThat(violations).hasSize(1);
        violations.stream().forEach(x -> logger.info(x.getMessage()));
    }

    /**
     * Tests that providing an empty resourceId and results in a validation error.
     */
    @Test
    public void testInvalidResourceId() {
        String invalidFilename = "";
        FileRestoreTaskDetails taskDetails = buildBaseFileRestoreTaskDetailsBuilder().resourceId(invalidFilename).build();

        Set<ConstraintViolation<FileRestoreTaskDetails>> violations = validator.validate(taskDetails);
        assertThat(violations).hasSize(1);
        violations.stream().forEach(x -> logger.info(x.getMessage()));
    }
}

