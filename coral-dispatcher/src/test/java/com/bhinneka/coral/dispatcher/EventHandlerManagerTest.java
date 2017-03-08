package com.bhinneka.coral.dispatcher;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class EventHandlerManagerTest {

  final Person zero = new Person("", 0);
  final PersonCreated personCreated = new PersonCreated("Didiet", 22);

  @Mock
  private EventHandler<Person> mockEventHandler;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testEventHandlerEmpty() {
    final EventHandlerManager<Person> eventHandlerManager = EventHandlerManager
        .<Person>newBuilder()
        .build();

    assertThat(eventHandlerManager.apply(zero, personCreated))
        .isEqualTo(zero);
  }

  @Test
  public void testEventHandlerNotFound() {
    final EventHandlerManager<Person> eventHandlerManager = EventHandlerManager
        .<Person>newBuilder()
        .addEventHandler(PersonCreated.class, mockEventHandler)
        .build();

    assertThat(eventHandlerManager.apply(zero, new PersonNameChanged("Didiet Noor")))
        .isEqualTo(zero);
  }

  @Test
  public void testEventHandlerFound() {
    when(mockEventHandler.apply(Mockito.<Person>any(), eq(personCreated)))
        .thenReturn(new Person(personCreated.getName(), personCreated.getAge()));

    final EventHandlerManager<Person> eventHandlerManager = EventHandlerManager
        .<Person>newBuilder()
        .addEventHandler(PersonCreated.class, mockEventHandler)
        .build();

    assertThat(eventHandlerManager.apply(zero, personCreated))
        .isEqualTo(new Person(personCreated.getName(), personCreated.getAge()));
  }
}
