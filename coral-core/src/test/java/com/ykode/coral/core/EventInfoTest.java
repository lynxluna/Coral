package com.ykode.coral.core;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;

import java.util.Date;
import java.util.UUID;

public class EventInfoTest {
  @Test
  public void eventInfoBuildTest() {
    final Person s = new Person("Didiet", 22);
    final UUID personId = UUID.fromString("045E7A36-DF79-45EF-8761-75B2869E168D");
    final Date fakeDate = new Date();
    final Event<Person> personCreated = new PersonCreated("Didiet", 22);


    final EventInfo<UUID, Person> eventInfo =
        EventInfo.newBuilder(personId, personCreated, 0).setDate(fakeDate).build();

    assertThat(eventInfo.getEntityId()).isEqualTo(personId);
    assertThat(eventInfo.getDate()).isEqualTo(fakeDate);
    assertThat(eventInfo.getEntityId()).isEqualTo(personId);
    assertThat(eventInfo.getEvent()).isEqualTo(personCreated);

  }
}
