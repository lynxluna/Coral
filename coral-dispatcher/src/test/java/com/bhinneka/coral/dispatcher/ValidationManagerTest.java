package com.bhinneka.coral.dispatcher;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

import com.bhinneka.coral.dispatcher.exceptions.ValidationException;
import org.assertj.core.api.ThrowableAssert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ValidationManagerTest {

  @Mock
  private Validator<Person> mockValidator;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testValidation() {
    final ValidationManager<Person> validationManager = ValidationManager.<Person>newBuilder()
        .addValidator(CreatePerson.class, mockValidator)
        .build();

    validationManager.validate(new Person("", 0), new CreatePerson("Didiet", 22));

    verify(mockValidator, times(1))
        .validate(eq(new Person("", 0)), eq(new CreatePerson("Didiet", 22)));
  }

  @Test
  public void testValidationException() {
    when(mockValidator.validate(eq(new Person("", 0)), any(ChangePersonName.class)))
        .thenThrow(new ValidationException(new ChangePersonName("Didiet"), new Person("", 0)));

    final ValidationManager<Person> validationManager = ValidationManager
        .<Person>newBuilder()
        .addValidator(ChangePersonName.class, mockValidator)
        .build();

    assertThatThrownBy(new ThrowableAssert.ThrowingCallable() {
      @Override
      public void call() throws Throwable {
        validationManager.validate(new Person("", 0), new ChangePersonName("Didiet"));
      }
    }).isInstanceOf(ValidationException.class);
  }
}
