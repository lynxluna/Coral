package com.bhinneka.coral.dispatcher;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import static org.mockito.Mockito.*;

import com.bhinneka.coral.core.Command;
import com.bhinneka.coral.core.Event;
import com.bhinneka.coral.dispatcher.exceptions.ExecutorEmptyException;
import com.bhinneka.coral.dispatcher.exceptions.ExecutorNotFoundException;
import com.bhinneka.coral.dispatcher.exceptions.ValidationException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

public class DispatchedAggregateTest {
  final Person zero = new Person("", 0);
  final PersonCreated personCreated = new PersonCreated("Didiet", 22);
  final CreatePerson createPerson = new CreatePerson("Didiet", 22);
  final ChangePersonName changePersonName = new ChangePersonName("Didiet Noor");
  final List<Event<Person>> personCreatedList =
      Collections.singletonList((Event<Person>) personCreated);

  @Mock
  private Executor<Person> mockExecutor;

  @Mock
  private Validator<Person> mockValidator;

  @Mock
  private EventHandler<Person> mockEventHandler;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testExecuteAggregateEmpty() {
    final DispatchedAggregate<Person> aggregate = DispatchedAggregate.<Person>newBuilder(zero)
        .build();

    assertThatThrownBy(new ThrowingCallable() {
      @Override
      public void call() throws Throwable {
        aggregate.exec(zero, createPerson);
      }
    }).isInstanceOf(ExecutorEmptyException.class);
  }

  @Test
  public void testExecuteAggregateNoExecutor() {
    final DispatchedAggregate<Person> aggregate = DispatchedAggregate.<Person>newBuilder(zero)
        .addExecutor(CreatePerson.class, mockExecutor)
        .build();

    assertThatThrownBy(new ThrowingCallable() {
      @Override
      public void call() throws Throwable {
        aggregate.exec(zero, changePersonName);
      }
    }).isInstanceOf(ExecutorNotFoundException.class);

    verify(mockExecutor, never()).execute(Mockito.<Person>any(), eq(changePersonName));
  }

  @Test
  public void testExecuteAggregateFailedValidation() {

    when(mockValidator.validate(eq(zero), eq(changePersonName)))
        .thenThrow(new ValidationException(changePersonName, zero, "Cannot Change State on Zero"));

    final DispatchedAggregate<Person> aggregate = DispatchedAggregate.<Person>newBuilder(zero)
        .addExecutor(ChangePersonName.class, mockExecutor)
        .addValidator(ChangePersonName.class, mockValidator)
        .build();

    assertThatThrownBy(new ThrowingCallable() {
      @Override
      public void call() throws Throwable {
        aggregate.exec(zero, changePersonName);
      }
    }).isInstanceOf(ValidationException.class)
        .hasMessageContaining("Cannot Change State on Zero");
  }

  @Test
  public void testExecuteAggregateSuccess() {
    when(mockValidator.validate(eq(zero), eq(createPerson))).thenReturn(true);
    when(mockExecutor.execute(eq(zero), eq(createPerson))).thenReturn(personCreatedList);

    final DispatchedAggregate<Person> aggregate = DispatchedAggregate.<Person>newBuilder(zero)
        .addExecutor(CreatePerson.class, mockExecutor)
        .addValidator(CreatePerson.class, mockValidator)
        .build();

    assertThat(aggregate.exec(zero, createPerson)).isEqualTo(personCreatedList);

    verify(mockValidator, times(1)).validate(eq(zero), eq(createPerson));
    verify(mockExecutor, times(1)).execute(eq(zero), eq(createPerson));

  }
}
