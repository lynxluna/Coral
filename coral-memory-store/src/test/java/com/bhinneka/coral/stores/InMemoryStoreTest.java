package com.bhinneka.coral.stores;

import com.bhinneka.coral.core.EventInfo;
import com.bhinneka.coral.core.handlers.AsyncHandler;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class InMemoryStoreTest {

  @Captor
  private ArgumentCaptor<UUID> successCaptorCommit;

  @Captor
  private ArgumentCaptor<List<EventInfo<UUID, Person>>> successCaptorLoad;

  @Mock
  private AsyncHandler<UUID> commitReceiver;

  @Mock
  private AsyncHandler<List<EventInfo<UUID, Person>>> retreiveReceiver;

  private final UUID fakeId = UUID.fromString("DAA20869-CEDF-4369-80F6-88B2E6BC9CD7");

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testInMemoryCommit() {

    // Given
    final InMemoryEventStore<UUID, Person> stores = new InMemoryEventStore<UUID, Person>();
    final PersonCreated personCreated = new PersonCreated("Didiet", 22);
    final EventInfo<UUID, Person> info = EventInfo.<UUID, Person>newBuilder(fakeId,
        personCreated, 0).build();

    // When
    stores.commit(info, commitReceiver);

    // Then
    verify(commitReceiver, times(1)).onSuccess(successCaptorCommit.capture());

    final UUID value = successCaptorCommit.getValue();
    assertThat(value).isNotNull();
    assertThat(value).isEqualTo(fakeId);
  }

  @Test
  public void testInMemoryRetreive() {
    // Given
    final InMemoryEventStore<UUID, Person> stores = new InMemoryEventStore<UUID, Person>();
    final PersonCreated personCreated = new PersonCreated("Didiet", 22);
    final PersonNameChanged personNameChanged = new PersonNameChanged("Didiet Noor");
    final EventInfo<UUID, Person> personCreatedInfo = EventInfo.<UUID, Person>newBuilder(fakeId,
        personCreated, 0).build();
    final EventInfo<UUID, Person> personNameChangedInfo = EventInfo.<UUID, Person>newBuilder(fakeId,
        personNameChanged, 1).build();


    stores.commit(personCreatedInfo, commitReceiver);
    stores.commit(personNameChangedInfo, commitReceiver);

    // When
    stores.load(fakeId, retreiveReceiver);

    // Then
    verify(retreiveReceiver, never()).onError(Mockito.<Exception>any());
    verify(retreiveReceiver, times(1)).onSuccess(successCaptorLoad.capture());
    verify(commitReceiver, times(2)).onSuccess(Mockito.<UUID>any());

    final List<EventInfo<UUID, Person>> eventInfos = successCaptorLoad.getValue();
    assertThat(eventInfos.size()).isEqualTo(2);
    assertThat(eventInfos.get(0)).isEqualTo(personCreatedInfo);
    assertThat(eventInfos.get(1)).isEqualTo(personNameChangedInfo);

  }

  @Test
  public void testInMemoryRetreiveVersion() {
    // Given
    final InMemoryEventStore<UUID, Person> stores = new InMemoryEventStore<UUID, Person>();
    final PersonCreated personCreated = new PersonCreated("Didiet", 22);
    final PersonNameChanged personNameChanged = new PersonNameChanged("Didiet Noor");
    final EventInfo<UUID, Person> personCreatedInfo = EventInfo.<UUID, Person>newBuilder(fakeId,
        personCreated, 0).build();
    final EventInfo<UUID, Person> personNameChangedInfo = EventInfo.<UUID, Person>newBuilder(fakeId,
        personNameChanged, 1).build();


    stores.commit(personCreatedInfo, commitReceiver);
    stores.commit(personNameChangedInfo, commitReceiver);

    // When
    stores.load(fakeId, 0, retreiveReceiver);

    // Then
    verify(retreiveReceiver, never()).onError(Mockito.<Exception>any());
    verify(retreiveReceiver, times(1)).onSuccess(successCaptorLoad.capture());
    verify(commitReceiver, times(2)).onSuccess(Mockito.<UUID>any());

    final List<EventInfo<UUID, Person>> eventInfos = successCaptorLoad.getValue();
    assertThat(eventInfos.size()).isEqualTo(1);
    assertThat(eventInfos.get(0)).isEqualTo(personCreatedInfo);
  }
}
