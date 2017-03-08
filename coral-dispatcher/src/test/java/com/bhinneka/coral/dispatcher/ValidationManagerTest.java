package com.bhinneka.coral.dispatcher;

import static org.mockito.Mockito.*;

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
}
