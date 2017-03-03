package com.bhinneka.coral.codecs.protobuf;

import static com.bhinneka.coral.protocols.EntityIDProtocol.EntityID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.assertj.core.api.ThrowableAssert;
import org.junit.Test;
import java.util.UUID;

final class SomeRandomObject {

}

public class EntityIDCodecProtobufTest {
  final private String uuidStr = "02F1FDCD-6DAC-4210-9350-A3CAACA7A5F4";
  final private int intId = 2023;
  final private String strId = "my-id-is-unique";
  final private UUID uuidId = UUID.fromString(uuidStr);
  final private EntityIDCodecProtobuf codec = new EntityIDCodecProtobuf();
  final private SomeRandomObject invalidId = new SomeRandomObject();

  @Test
  public void testEntityIDEncodeUUID() {
    final EntityID eid = codec.encode(uuidId);
    assertThat(eid.getType()).isEqualTo(EntityID.Type.UUID);
    assertThat(eid.getUuidId().toLowerCase()).isEqualTo(uuidStr.toLowerCase());

    final UUID entityId = codec.decode(eid);
    assertThat(entityId).isEqualTo(uuidId);
  }

  @Test
  public void testEntityIDEncodeString() {
    final EntityID eid = codec.encode(strId);
    assertThat(eid.getType()).isEqualTo(EntityID.Type.STRING);
    assertThat(eid.getStringId()).isEqualTo(strId);

    final String entityId = codec.decode(eid);
    assertThat(entityId).isEqualTo(strId);
  }

  @Test
  public void testEntityIDEncodeInt() {
    final EntityID eid = codec.encode(intId);
    assertThat(eid.getType()).isEqualTo(EntityID.Type.INTEGER);
    assertThat(eid.getIntegerId()).isEqualTo(intId);

    final Integer entityId = codec.decode(eid);
    assertThat(entityId).isEqualTo(intId);
  }

  @Test
  public void testRandomObject() {
    assertThatThrownBy(new ThrowableAssert.ThrowingCallable() {
      @Override
      public void call() throws Throwable {
        final EntityID eid = codec.encode(invalidId);
      }
    }).isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("needs to be UUID, String or Integer");
  }

  @Test
  public void testNonRegisteredMessage() {
    final EntityID invalidEid = EntityID.newBuilder().build();
    assertThat(codec.decode(invalidEid)).isNull();
  }
}
