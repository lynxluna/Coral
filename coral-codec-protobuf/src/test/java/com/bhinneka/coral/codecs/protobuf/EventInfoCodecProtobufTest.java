package com.bhinneka.coral.codecs.protobuf;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.in;

import com.bhinneka.coral.core.Event;
import com.bhinneka.coral.core.EventInfo;
import com.bhinneka.coral.protocols.EventInfoProtocol;
import com.bhinneka.coral.protocols.test.PersonProtocol;
import com.example.user.Person;
import com.example.user.events.PersonCreated;
import com.google.protobuf.Any;
import com.google.protobuf.InvalidProtocolBufferException;
import org.junit.Test;

import java.util.Date;
import java.util.UUID;

public class EventInfoCodecProtobufTest {
  final Event<Person> personCreated = new PersonCreated("Didiet", 22);
  final EventInfoCodecProtobuf codec = new EventInfoCodecProtobuf();

  @Test
  public void testEncodeEvent() throws InvalidProtocolBufferException {
    final Date timestamp = new Date();
    final UUID fakeUUID = UUID.fromString("2FA639D6-E118-4606-A9CE-43ACE249EEC4");

    @SuppressWarnings("unchecked")
    final EventInfo<UUID, Person> eventInfo = EventInfo.<UUID, Person>newBuilder(fakeUUID, personCreated, 0)
        .setDate(timestamp)
        .build();

    EventInfoProtocol.EventInfo info = codec.encode(eventInfo);
    assertThat(info).isNotNull();
    assertThat(info).isInstanceOf(EventInfoProtocol.EventInfo.class);
    assertThat(info.getEntityId()).isNotNull();
    assertThat(info.getDetails()).isInstanceOf(Any.class);
    assertThat(info.getDetails().unpack(PersonProtocol.PersonCreated.class)).isNotNull();
  }

  @Test
  public void testDecodeEvent() throws Exception {
    final Date timestamp = new Date();
    final UUID fakeUUID = UUID.fromString("2FA639D6-E118-4606-A9CE-43ACE249EEC4");

    @SuppressWarnings("unchecked")
    final EventInfo<UUID, Person> eventInfo = EventInfo.<UUID, Person>newBuilder(fakeUUID, personCreated, 0)
        .setDate(timestamp)
        .build();

    EventInfoProtocol.EventInfo info = codec.encode(eventInfo);

    EventInfo<UUID, Person> out = codec.decode(info, PersonCreated.class);

    assertThat(out).isNotNull();
    assertThat(out).isEqualTo(eventInfo);
    assertThat(out.getEvent()).isEqualTo(eventInfo.getEvent());
  }
}
