package com.ykode.coral.core;

import com.ykode.coral.core.handlers.AsyncHandler;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;



public class CommandHandlerTest {

  @Mock
  private EventStore<UUID, Person> mockStore;

  @Mock
  private Aggregate<Person> mockAggregate;

  @Mock
  private IdGenerator<UUID> mockGenerator;

  @Mock
  private AsyncHandler<List<EventInfo<UUID, Person>>> receiver;

  @Captor
  private ArgumentCaptor<List<EventInfo<UUID, Person>>> successCaptor;

  @Captor
  private ArgumentCaptor<Exception> errorCaptor;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testCommandHandlerCreation() throws Exception{

    // Given
    final UUID fakeUUID = UUID.fromString("CB6CA9A0-6DDA-4CDB-82A8-639753740B5F");
    final Command<Person> createPerson = new CreatePerson("Didiet", 29);
    final Event<Person> personCreated  = new PersonCreated("Didiet", 29);
    final EventInfo<UUID, Person> personInfo = EventInfo.newBuilder(fakeUUID, personCreated, 0).build();

    // prepare the mocks
    when(mockGenerator.nextValue()).thenReturn(fakeUUID);
    when(mockAggregate.exec(Mockito.<Person>any(), eq(createPerson))).thenReturn(Collections.singletonList(personCreated));

    // When
    final CommandHandler<UUID, Person> cmdHandler =
        new CommandHandler<UUID, Person>(mockAggregate, mockStore, mockGenerator);
    cmdHandler.handle(null, createPerson, receiver);

    // Then
    verify(mockGenerator, times(1)).nextValue();
    verify(mockAggregate, times(1)).exec(Mockito.<Person>any(), eq(createPerson));
    verify(receiver, times(1)).onSuccess(successCaptor.capture());

    final EventInfo<UUID, Person> theInfo = successCaptor.getValue().get(0);

    assertThat(theInfo.getEntityId()).isEqualTo(personInfo.getEntityId());
    assertThat(theInfo.getEvent()).isEqualTo(personInfo.getEvent());
    assertThat(theInfo.getVersion()).isEqualTo(personInfo.getVersion());

    verify(receiver, never()).onError(Mockito.<Exception>any());
  }

  @Test
  public void testCommandHandlerCreationFailed() throws Exception {
    // Given
    when(mockAggregate.exec(Mockito.<Person>any(), Mockito.<Command<Person>>any()))
        .thenThrow(new Exception("Invalid Command"));

    // When
    final CommandHandler<UUID, Person> cmdHandler =
        new CommandHandler<UUID, Person>(mockAggregate, mockStore, mockGenerator);
    cmdHandler.handle(null, new CreatePerson("Noor", 100), receiver);

    // Then
    verify(receiver, times(1)).onError(errorCaptor.capture());
    assertThat(errorCaptor.getValue()).hasMessageContaining("Invalid Command");
  }
}
