package com.ykode.coral.core;

import java.util.UUID;
import org.junit.Test;
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
  }
}
