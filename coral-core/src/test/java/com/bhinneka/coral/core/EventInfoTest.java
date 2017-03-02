package com.bhinneka.coral.core;

import org.junit.Test;

import java.util.Date;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class EventInfoTest {
  @Test
  public void eventInfoBuildTest() {
    final Person s = new Person("Didiet", 22);
    final UUID personId = UUID.fromString("045E7A36-DF79-45EF-8761-75B2869E168D");
    final Date fakeDate = new Date();
    final Event<Person> personCreated = new PersonCreated("Didiet", 22);

    @SuppressWarnings("unchecked")
    final EventInfo<UUID, Person> eventInfo =
        EventInfo.newBuilder(personId, personCreated, 0).setDate(fakeDate).build();

    assertThat(eventInfo.getEntityId()).isEqualTo(personId);
    assertThat(eventInfo.getDate()).isEqualTo(fakeDate);
    assertThat(eventInfo.getEntityId()).isEqualTo(personId);
    assertThat(eventInfo.getEvent()).isEqualTo(personCreated);

  }

  @Test
  public void eventInfoCopyTest() {
    final Person s = new Person("Didiet", 22);
    final UUID personId = UUID.fromString("045E7A36-DF79-45EF-8761-75B2869E168D");
    final Date fakeDate = new Date();
    final Event<Person> personCreated = new PersonCreated("Didiet", 22);

    @SuppressWarnings("unchecked")
    final EventInfo<UUID, Person> eventInfo =
        EventInfo.newBuilder(personId, personCreated, 0).setDate(fakeDate).build();

    @SuppressWarnings("unchecked")
    final EventInfo<UUID, Person> evi2 = eventInfo.copyBuilder()
        .setDate(new Date())
        .setEvent(new PersonNameChanged("Didiet Noor"))
        .setVersion(1)
        .setEntityId(personId)
        .build();

    assertThat(evi2.getEvent()).isInstanceOf(PersonNameChanged.class);
    assertThat(evi2).isNotEqualTo(eventInfo);
  }
}
