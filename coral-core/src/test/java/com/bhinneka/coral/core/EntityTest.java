package com.bhinneka.coral.core;

import org.junit.Test;

import java.util.Collections;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


public class EntityTest {

  @Test
  public void testEntityEquality() {
    final Person s1 = new Person("Didiet", 20);
    final Person s2 = new Person("Didiet", 22);
    final UUID personId = UUID.fromString("045E7A36-DF79-45EF-8761-75B2869E168D");

    final Entity<UUID, Person> p1 = new Entity<UUID, Person>(personId, s1, 0);
    final Entity<UUID, Person> p2 = new Entity<UUID, Person>(personId, s2, 1);

    assertThat(p1).isEqualTo(p2);
    assertThat(Collections.singletonList(p1)).contains(p2);
    assertThat(p1.getState()).isEqualTo(s1);
    assertThat(p1.getId()).isEqualTo(personId);
    assertThat(p1.getVersion()).isEqualTo(0);
  }
}
