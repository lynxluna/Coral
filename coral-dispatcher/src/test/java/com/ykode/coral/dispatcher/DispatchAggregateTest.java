package com.ykode.coral.dispatcher;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

import com.ykode.coral.core.Event;
import com.ykode.coral.core.exceptions.InvalidCommandException;
import com.ykode.coral.dispatcher.exceptions.ExecutorEmptyException;
import com.ykode.coral.dispatcher.exceptions.ExecutorNotFoundException;
import org.assertj.core.api.ThrowableAssert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

public class DispatchAggregateTest {

  // SUT
  private final DispatchAggregate<Person> personDispatch =
      new DispatchAggregate<Person>(new Person("",  0));

  @Mock
  private Executor<Person> mockExecutor;

  @Mock
  private Validator<Person> mockValidator;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testExecutorValidator() {
    // Given
    final Person zero = new Person("", 0);
    final CreatePerson createPerson = new CreatePerson("Didiet", 22);
    final PersonCreated personCreated = new PersonCreated("Didiet", 22);
    final ChangePersonName changeName = new ChangePersonName("Didiet Noor");

    when(mockValidator.validate(zero, createPerson))
        .thenReturn(true);

    when(mockValidator.validate(zero, changeName))
        .thenThrow(new InvalidCommandException(changeName, zero));

    when(mockExecutor.execute(zero, createPerson))
        .thenReturn(Collections.singletonList((Event<Person>)personCreated));


    // Then
    assertThatThrownBy(new ThrowableAssert.ThrowingCallable() {
      @Override
      public void call() throws Throwable {
        personDispatch.exec(zero, createPerson);
      }
    }).isInstanceOf(ExecutorEmptyException.class);

    // When
    personDispatch.addHandler(CreatePerson.class, mockExecutor, mockValidator);
    final List<Event<Person>> eventList = personDispatch.exec(zero, createPerson);

    // Then
    verify(mockValidator, times(1))
        .validate(zero, createPerson);
    verify(mockExecutor, times(1))
        .execute(zero, createPerson);

    assertThat(eventList).containsOnlyOnce(personCreated);
    assertThat(eventList).isNotEmpty();

    // When
    personDispatch.addHandler(ChangePersonName.class, mockExecutor, mockValidator);

    // Then
    assertThatThrownBy(new ThrowableAssert.ThrowingCallable() {
      @Override
      public void call() throws Throwable {
        personDispatch.exec(zero, changeName);
      }
    }).isInstanceOf(InvalidCommandException.class)
        .hasMessageContaining("Invalid command");
  }
}
