package com.ykode.coral.core;

import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;


public class CommandInfoTest {

  @Test
  public void testCommandInfoCreation() {
    final CommandInfo<UUID, Person> commandInfo =
        CommandInfo.<UUID, Person>newBuilder(new CreatePerson("name", 11)).build();
    final UUID fakeUUID = UUID.fromString("A0D411D2-1203-448B-925C-3E294C5FA63C");

    assertThat(commandInfo.getCommand()).isInstanceOf(CreatePerson.class);
    assertThat(commandInfo.getEntityId()).isNull();
    assertThat(commandInfo.getTargetVersion()).isZero();

    final CommandInfo<UUID, Person> ci2 = commandInfo.copyBuilder()
        .setCommand(new ChangePersonName("noname"))
        .setEntityId(fakeUUID)
        .setTargetVersion(0)
        .build();

    assertThat(ci2.getCommand()).isInstanceOf(ChangePersonName.class);
    assertThat(ci2.getEntityId()).isEqualTo(fakeUUID);
    assertThat(ci2).isNotEqualTo(commandInfo);
  }
}
