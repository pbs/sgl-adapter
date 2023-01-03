package org.pbs.sgladapter.model.sgl;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SglFilesPayloadTest {
  private static final Logger logger = LoggerFactory.getLogger(SglFilesPayloadTest.class);
  private static Validator validator;

  private static SglFilesPayload.SglFilesPayloadBuilder buildBaseSglFilePayloadBuilder() {
    return SglFilesPayload.builder()
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
    SglFilesPayload sglFilesPayload = buildBaseSglFilePayloadBuilder().build();
    Set<ConstraintViolation<SglFilesPayload>> violations = validator.validate(sglFilesPayload);
    assertThat(violations).hasSize(0);
  }

  /**
   * Tests that providing an empty filename and results in a validation error.
   */
  @Test
  public void tesValidEmptyFilename() {
    String invalidFilename = "";
    SglFilesPayload sglFilesPayload =
        buildBaseSglFilePayloadBuilder().filename(invalidFilename).build();

    Set<ConstraintViolation<SglFilesPayload>> violations = validator.validate(sglFilesPayload);
    assertThat(violations).hasSize(0);
  }

  /**
   * Tests that providing an empty resourceId and results in a validation error.
   */
  @Test
  public void testInvalidGuid() {
    String invalidFilename = "";
    SglFilesPayload sglFilesPayload =
        buildBaseSglFilePayloadBuilder().guid(invalidFilename).build();

    Set<ConstraintViolation<SglFilesPayload>> violations = validator.validate(sglFilesPayload);
    assertThat(violations).hasSize(1);
    violations.stream().forEach(x -> logger.info(x.getMessage()));
  }
}

