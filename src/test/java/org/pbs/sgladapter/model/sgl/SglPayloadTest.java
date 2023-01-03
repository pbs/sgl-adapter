package org.pbs.sgladapter.model.sgl;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SglPayloadTest {
  private static final Logger logger = LoggerFactory.getLogger(SglPayloadTest.class);
  private static Validator validator;

  private static SglPayload.SglPayloadBuilder buildBaseSGLFilePayloadBuilder() {
    return SglPayload.builder()
        .caller("123e4567-e89b-12d3-a456-9AC7CBDCEE52")
        .displayName("P123123-001")
        .priority(1)
        .files(List.of(new SglFilesPayload("P123123-001",
            "\\\\m-isilonsmb\\gpop_dev\\mxf",
            "P123123-001.mxf", "")));
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
    SglPayload sglPayload = buildBaseSGLFilePayloadBuilder().build();
    Set<ConstraintViolation<SglPayload>> violations = validator.validate(sglPayload);
    assertThat(violations).hasSize(0);
  }

  /**
   * Tests that providing an empty caller and results in a validation error.
   */
  @Test
  public void testInvalidCaller() {
    String invalidCaller = "";
    SglPayload sglPayload = buildBaseSGLFilePayloadBuilder().caller(invalidCaller).build();

    Set<ConstraintViolation<SglPayload>> violations = validator.validate(sglPayload);
    assertThat(violations).hasSize(1);
    violations.stream().forEach(x -> logger.info(x.getMessage()));
  }


  /**
   * Tests that providing an empty display name and it does not result in a validation error.
   */
  @Test
  public void tesValidEmptyDisplayName() {
    String emptyDisplayName = "";
    SglPayload sglPayload = buildBaseSGLFilePayloadBuilder().displayName(emptyDisplayName).build();

    Set<ConstraintViolation<SglPayload>> violations = validator.validate(sglPayload);
    assertThat(violations).hasSize(0);
  }

  /**
   * Tests that providing invalid priority and results in a validation error.
   */
  @Test
  public void testInvalidPriority() {
    Integer invalidPriority = 6;
    SglPayload sglPayload = buildBaseSGLFilePayloadBuilder().priority(invalidPriority).build();

    Set<ConstraintViolation<SglPayload>> violations = validator.validate(sglPayload);
    assertThat(violations).hasSize(1);
    violations.stream().forEach(x -> logger.info(x.getMessage()));
  }


  /**
   * Tests that providing invalid files and results in a validation error.
   */
  @Test
  public void testInvalidFiles() {
/**        SGLFilesPayload invalidFiles = null;
 SGLPayload sglPayload = buildBaseSGLFilePayloadBuilder().files(List.of(invalidFiles)).build();

 Set<ConstraintViolation<SGLPayload>> violations = validator.validate(sglPayload);
 assertThat(violations).hasSize(1);
 violations.stream().forEach(x -> logger.info(x.getMessage()));
 */
  }

}

