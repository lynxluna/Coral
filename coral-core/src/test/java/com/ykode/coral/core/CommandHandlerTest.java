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

// dummy state class
class Person {
  private final String name;
  private final int age;


  public Person(String name, int age) {
    this.name = name; this.age = age;
  }

  public int getAge() {
    return age;
  }

  public String getName() {
    return name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Person person = (Person) o;

    if (age != person.age) return false;
    return name != null ? name.equals(person.name) : person.name == null;
  }

  @Override
  public int hashCode() {
    int result = name != null ? name.hashCode() : 0;
    result = 31 * result + age;
    return result;
  }
}

class CreatePerson implements Command<Person> {
  private final String name;
  private final int age;

  public CreatePerson(String name, int age) {
    this.name = name;
    this.age = age;
  }

  public String getName() {
    return name;
  }

  public int getAge() {
    return age;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    CreatePerson that = (CreatePerson) o;

    if (age != that.age) return false;
    return name.equals(that.name);
  }

  @Override
  public int hashCode() {
    int result = name.hashCode();
    result = 31 * result + age;
    return result;
  }
}

class PersonCreated implements Event<Person> {
  private final String name;
  private final int age;

  public PersonCreated(String name, int age) {
    this.name = name;
    this.age = age;
  }

  public String getName() {
    return name;
  }

  public int getAge() {
    return age;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    PersonCreated that = (PersonCreated) o;

    if (age != that.age) return false;
    return name.equals(that.name);
  }

  @Override
  public int hashCode() {
    int result = name.hashCode();
    result = 31 * result + age;
    return result;
  }
}

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

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testCommandHandlerCreation() {

    // Given
    final UUID fakeUUID = UUID.fromString("CB6CA9A0-6DDA-4CDB-82A8-639753740B5F");
    final Command<Person> createPerson = new CreatePerson("Didiet", 29);
    final Event<Person> personCreated  = new PersonCreated("Didiet", 29);
    final EventInfo<UUID, Person> personInfo = EventInfo.newBuilder(fakeUUID, personCreated, 0).build();
    final List<EventInfo<UUID, Person>> eventInfos = Collections.singletonList(personInfo);

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
}
