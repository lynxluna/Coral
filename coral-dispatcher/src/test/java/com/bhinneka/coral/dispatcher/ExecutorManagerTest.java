package com.bhinneka.coral.dispatcher;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

import com.bhinneka.coral.core.Command;
import com.bhinneka.coral.dispatcher.exceptions.ExecutorEmptyException;
import com.bhinneka.coral.dispatcher.exceptions.ExecutorNotFoundException;
import org.assertj.core.api.ThrowableAssert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class ExecutorManagerTest {

  @Mock
  private Executor<Person> personMockExecutor;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testEmptyExecutor() {
    final ExecutorManager<Person> executorManager = ExecutorManager.<Person>newBuilder().build();

    assertThatThrownBy(new ThrowableAssert.ThrowingCallable() {
      @Override
      public void call() throws Throwable {
        executorManager.execute(new Person("", 0), new CreatePerson("Didiet", 22));
      }
    }).isInstanceOf(ExecutorEmptyException.class);
  }

  @Test
  public void testNoHandler() {
    final ExecutorManager<Person> executorManager = ExecutorManager
        .<Person>newBuilder()
        .addExecutor(CreatePerson.class, personMockExecutor)
        .build();

    assertThatThrownBy(new ThrowableAssert.ThrowingCallable() {
      @Override
      public void call() throws Throwable {
        executorManager.execute(new Person("Didiet", 22), new ChangePersonName("Didiet Noor"));
      }
    }).isInstanceOf(ExecutorNotFoundException.class);

    verify(personMockExecutor, never()).execute(Mockito.<Person>any(), Mockito.<Command<Person>>any());
  }

  @Test
  public void testHandler() {
    final ExecutorManager<Person> executorManager = ExecutorManager
        .<Person>newBuilder()
        .addExecutor(CreatePerson.class, personMockExecutor)
        .build();

    executorManager.execute(new Person("",0), new CreatePerson("Didiet", 22));

    verify(personMockExecutor, times(1))
        .execute(eq(new Person("", 0)), eq(new CreatePerson("Didiet", 22)));

  }
}
