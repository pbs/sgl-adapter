package org.pbs.sgladapter.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import lombok.experimental.SuperBuilder;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TaskTest {
  private static final Logger logger = LoggerFactory.getLogger(TaskTest.class);
  private static Validator validator;

  private static Task.TaskBuilder buildBaseTaskBuilder() {
    return MockTask.builder().type("FileRestore")
        .correlationId("123e4567-e89b-12d3-a456-9AC7CBDCEE52").priority(2);
  }

  @BeforeAll
  static void setUp() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  /**
   * Tests that providing a valid TaskType for a Task does not result in a validation error.
   */
  @Test
  public void testValidTaskType() {
    String validType = "FileRestore";
    Task taskInputTask1 = buildBaseTaskBuilder().type(validType).build();

    Set<ConstraintViolation<Task>> violations = validator.validate(taskInputTask1);
    assertThat(violations).hasSize(0);
  }


  /**
   * Tests that providing an invalid TaskType for a Task results in the expected validation error.
   */
  @Test
  public void testInvalidTaskType() {
    String invalidType = "UnsupportedTaskType";
    Task taskInputTask1 = buildBaseTaskBuilder().type(invalidType).build();

    Set<ConstraintViolation<Task>> violations = validator.validate(taskInputTask1);
    assertThat(violations).hasSize(1);
    violations.stream().forEach(x -> logger.info(x.getMessage()));
  }

  /**
   * Tests that providing a valid CorrelationId for a Task does not result in a validation error.
   */
  @Test
  public void testValidCorrelationId() {
    String validCorrelationId = "123e4567-e89b-12d3-a456-9AC7CBDCEE52";
    Task taskInputTask1 = buildBaseTaskBuilder().correlationId(validCorrelationId).build();

    Set<ConstraintViolation<Task>> violations = validator.validate(taskInputTask1);
    assertThat(violations).hasSize(0);
  }

  /**
   * Tests that providing an invalid CorrelationId for a Task results in the expected validation
   * error.
   */
  @Test
  public void testInvalidCorrelationId() {
    // 'h' is not a valid character in the GUID format - e.g. ...e89h...
    String validCorrelationId = "123e4567-e89h-12d3-a456-9AC7CBDCEE52";
    Task taskInputTask1 = buildBaseTaskBuilder().correlationId(validCorrelationId).build();

    Set<ConstraintViolation<Task>> violations = validator.validate(taskInputTask1);
    assertThat(violations).hasSize(1);
    assertThat(violations.iterator().next().getMessage())
        .isEqualTo("correlationId must be a valid GUID");
    violations.stream().forEach(x -> logger.info(x.getMessage()));
  }


  /**
   * Tests that providing a valid priority for a Task does not result in a validation error.
   */
  @Test
  public void testValidPriority() {
    int validPriority = 3;
    Task taskInputTask1 = buildBaseTaskBuilder().priority(validPriority).build();

    Set<ConstraintViolation<Task>> violations = validator.validate(taskInputTask1);
    assertThat(violations).hasSize(0);
  }

  /**
   * Tests that providing an invalid priority for a Task results in the expected validation errors.
   */
  @Test
  public void testInvalidPriority() {
    int invalidMinPriority = 0;
    Task taskInputTask1 = buildBaseTaskBuilder().priority(invalidMinPriority).build();

    Set<ConstraintViolation<Task>> violations = validator.validate(taskInputTask1);
    assertThat(violations).hasSize(1);
    assertThat(violations.iterator().next().getMessage())
        .isEqualTo("priority must be greater than 0");
    violations.stream().forEach(x -> logger.info(x.getMessage()));

    taskInputTask1.setPriority(101);
    violations = validator.validate(taskInputTask1);
    assertThat(violations).hasSize(1);
    assertThat(violations.iterator().next().getMessage())
        .isEqualTo("priority must be less than 101");
    violations.stream().forEach(x -> logger.info(x.getMessage()));
  }

  /**
   * Tests that providing a string for a taskId does not result in a validation error.
   */
  @Test
  public void testValidStringTaskId() {
    String validTaskId = "ValidID";

    Task taskInputTask1 = buildBaseTaskBuilder().taskId(validTaskId).build();

    Set<ConstraintViolation<Task>> violations = validator.validate(taskInputTask1);
    assertThat(violations).hasSize(0);
  }

  /**
   * Tests that providing a null for a taskId does not result in a validation error.
   */
  @Test
  public void testValidNullTaskId() {
    String validTaskId = null;

    Task taskInputTask1 = buildBaseTaskBuilder().taskId(validTaskId).build();

    Set<ConstraintViolation<Task>> violations = validator.validate(taskInputTask1);
    assertThat(violations).hasSize(0);
  }

  /**
   * Tests that providing an empty string for a taskId does not result in a validation error.
   */
  @Test
  public void testValidEmptyTaskId() {
    String validTaskId = "";

    Task taskInputTask1 = buildBaseTaskBuilder().taskId(validTaskId).build();

    Set<ConstraintViolation<Task>> violations = validator.validate(taskInputTask1);
    assertThat(violations).hasSize(0);
  }

  /**
   * Verifies equals() and hashCode() methods with library.
   */
  @Test
  public void testEqualsAndHashCode() {
    EqualsVerifier.simple().forClass(Task.class).verify();
  }
}


@SuperBuilder
class MockTask extends Task {
}
