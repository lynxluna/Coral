package com.bhinneka.coral.dispatcher;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

import com.bhinneka.coral.core.Event;
import com.bhinneka.coral.core.exceptions.InvalidCommandException;
import com.bhinneka.coral.dispatcher.exceptions.EventHandlerNotFoundException;
import com.bhinneka.coral.dispatcher.exceptions.EventHandlerEmptyException;
import com.bhinneka.coral.dispatcher.exceptions.ExecutorEmptyException;
import com.bhinneka.coral.dispatcher.exceptions.ExecutorNotFoundException;
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

  @Mock EventHandler<Person> mockHandler;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testEventHandler() throws Exception {

    // Given
    final Person zero = new Person("", 0);
    final PersonCreated personCreated = new PersonCreated("Didiet", 22);
    final PersonNameChanged personNameChanged = new PersonNameChanged("Didiet Noor");

    when(mockHandler.apply(zero, personCreated))
        .thenReturn(new Person("Didiet", 22));

    // Then
    assertThatThrownBy(new ThrowableAssert.ThrowingCallable() {
      @Override
      public void call() throws Throwable {
        personDispatch.apply(zero, personCreated);
      }
    }).isInstanceOf(EventHandlerEmptyException.class)
        .hasMessageContaining("No Handler");

    // Given
    personDispatch.addHandler(PersonCreated.class, mockHandler);

    // When
    final Person p = personDispatch.apply(zero, personCreated);
    assertThatThrownBy(new ThrowableAssert.ThrowingCallable() {
      @Override
      public void call() throws Throwable {
        personDispatch.apply(zero, personNameChanged);
      }
    }).isInstanceOf(EventHandlerNotFoundException.class)
      .hasMessageContaining("There is no handler");

    // Then
    verify(mockHandler, times(1))
        .apply(eq(zero), eq(personCreated));
    assertThat(p.getName()).isEqualTo("Didiet");
    assertThat(p.getAge()).isEqualTo(22);

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

    // Then
    assertThatThrownBy(new ThrowableAssert.ThrowingCallable() {
      @Override
      public void call() throws Throwable {
        personDispatch.exec(zero, changeName);
      }
    }).isInstanceOf(ExecutorNotFoundException.class);

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
