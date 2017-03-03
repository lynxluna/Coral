package com.bhinneka.coral.codecs.protobuf;

import static com.bhinneka.coral.protocols.EntityIDProtocol.EntityID;

import com.bhinneka.coral.core.codecs.EntityIDCodec;
import com.bhinneka.coral.protocols.EntityIDProtocol;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

public final class EntityIDCodecProtobuf implements EntityIDCodec<EntityID> {
  @Override
  public EntityID encode(@Nonnull Object identity) throws IllegalArgumentException {
    EntityID.Builder protoBuider = EntityID.newBuilder();

    if (identity instanceof UUID) {
      protoBuider.setType(EntityID.Type.UUID);
      protoBuider.setUuidId(identity.toString());
    } else if (identity instanceof String ) {
      protoBuider.setType(EntityID.Type.STRING);
      protoBuider.setStringId((String) identity);
    } else if (identity instanceof Integer) {
      protoBuider.setType(EntityID.Type.INTEGER);
      protoBuider.setIntegerId(((Integer) identity).longValue());
    } else {
      throw new IllegalArgumentException("Identity" + identity.toString() + " needs to be UUID, String or Integer");
    }

    return protoBuider.build();
  }

  @Override
  @Nullable
  public <T> T decode(@Nonnull EntityIDProtocol.EntityID message) {
    switch (message.getType()) {
      case UUID: return (T) UUID.fromString(message.getUuidId());
      case STRING: return (T) message.getStringId();
      case INTEGER: return (T) new Integer((int) message.getIntegerId());
      default:
        return null;
    }
  }
}
