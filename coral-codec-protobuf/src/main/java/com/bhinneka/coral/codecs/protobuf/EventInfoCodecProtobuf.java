package com.bhinneka.coral.codecs.protobuf;

import com.bhinneka.coral.codecs.protobuf.annotations.ProtoClass;
import com.bhinneka.coral.core.Event;
import com.bhinneka.coral.core.EventInfo;
import com.bhinneka.coral.core.codecs.EntityIDCodec;
import com.bhinneka.coral.core.codecs.EventInfoCodec;
import com.bhinneka.coral.core.util.UnsafeAllocator;
import com.bhinneka.coral.protocols.EntityIDProtocol;
import com.bhinneka.coral.protocols.EventInfoProtocol;
import com.google.protobuf.Any;
import com.google.protobuf.Message;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

public class EventInfoCodecProtobuf implements EventInfoCodec<EventInfoProtocol.EventInfo> {
  private EntityIDCodec<EntityIDProtocol.EntityID> eidCodec;
  private DateTimeFormatter formatter;

  public EventInfoCodecProtobuf() {
    eidCodec = new EntityIDCodecProtobuf();
    formatter = ISODateTimeFormat.dateTime();
  }

  @Override
  public EventInfoProtocol.EventInfo encode(EventInfo<?, ?> info) {
    final Class<? extends Event> evtKlass = info.getEvent().getClass();
    final EventInfoProtocol.EventInfo.Builder builder
        = EventInfoProtocol.EventInfo.newBuilder();

    final DateTime dt = new DateTime(info.getDate());

    builder.setEntityId(eidCodec.encode(info.getEntityId()))
        .setTimestamp(dt.toString(formatter))
        .setVersion(info.getVersion());

    // Search for Event Proto Class
    final ProtoClass protoAnnotation =  evtKlass.getAnnotation(ProtoClass.class);
    final Class<? extends Message> protoKlass = protoAnnotation.value();
    try {
      final Method newBuilderMethod = protoKlass.getDeclaredMethod("newBuilder", null);
      final Message.Builder msg = (Message.Builder) newBuilderMethod.invoke(null, null);
      final Field[] objectFields = evtKlass.getDeclaredFields();
      for(final Field f : objectFields) {
        if (f.isSynthetic()) {
          continue; // skip synthetic fields
        }

        final boolean accessible = f.isAccessible();
        final String fieldName = f.getName();
        final String protoSetter = "set" +
            fieldName.substring(0,1).toUpperCase() +
            fieldName.substring(1);

        try {
          f.setAccessible(true);
          final Object v = f.get(info.getEvent());

          if (v == null) {
            continue;
          }
          final Class<?> builderKlass = msg.getClass();

          Class<?> paramKlass;
          Object paramObject;

          // Need to document this and create
          // custom transformer

          if (v instanceof String) {
            paramKlass = String.class;
            paramObject = v;
          } else if(v instanceof URL) {
            paramKlass = String.class;
            paramObject = v.toString();
          } else if(v instanceof Float) {
            paramKlass = float.class;
            paramObject = v;
          } else if(v instanceof Integer) {
            paramKlass = int.class;
            paramObject = v;
          } else if(v instanceof Long) {
            paramKlass = long.class;
            paramObject = v;
          } else if(v instanceof Double) {
            paramKlass = double.class;
            paramObject = v;
          } else {
            paramKlass = Object.class;
            paramObject = v;
          }

          final Method setterMethod = builderKlass.getDeclaredMethod(protoSetter, paramKlass);
          setterMethod.invoke(msg, paramObject);
        }
        catch (IllegalAccessException e) {
          continue;
        }
        catch (NoSuchMethodException e) {
          continue;
        }
        f.setAccessible(accessible);
      }

      builder.setDetails(Any.pack(msg.build()));

    } catch(NoSuchMethodException e) {
      // silent
    } catch(IllegalAccessException e) {
      // silent
    } catch(InvocationTargetException e) {
      // silent
    }
    
    // Build
    return builder.build();
  }

  @Override
  public final <I, S> EventInfo<I, S> decode(EventInfoProtocol.EventInfo msg, Class<? extends Event<S>> eventKlass)
      throws Exception {

    final ProtoClass annotation = eventKlass.getAnnotation(ProtoClass.class);
    final Class<? extends Message> evtProtoKlass = annotation.value();
    final Message evtMessage = msg.getDetails().unpack(evtProtoKlass);
    final UnsafeAllocator allocator = UnsafeAllocator.create();
    try {
      final Event<S> event = allocator.newInstance(eventKlass);
      final Field[] fields = eventKlass.getDeclaredFields();

      for (final Field f : fields) {
        if (f.isSynthetic()) {
          continue;
        }

        final boolean accessible = f.isAccessible();
        final String fieldName = f.getName();
        final String protoAccessorName = "get" +
            fieldName.substring(0, 1).toUpperCase() +
            fieldName.substring(1);

        try {
          final Method protoAccessorMethod = evtProtoKlass.getDeclaredMethod(protoAccessorName, null);
          f.setAccessible(true);
          final Object protoValue = protoAccessorMethod.invoke(evtMessage);

          if (f.getType().isAssignableFrom(URL.class)) {
            final String urlStr = (String) protoValue;
            f.set(event, new URL(urlStr));
          } else {
            f.set(event, protoValue);
          }
          f.setAccessible(accessible);
        } catch (NoSuchMethodException e) {
          continue;
        } catch (IllegalAccessException e) {
          continue;
        } catch (MalformedURLException e) {
          continue;
        }
      }

      final I entityId = eidCodec.decode(msg.getEntityId());

      if (entityId != null) {
        final Date timestamp = formatter.parseDateTime(msg.getTimestamp()).toDate();
        final int version = (int) msg.getVersion();

        @SuppressWarnings("unchecked") final EventInfo<I, S> ei = EventInfo.<I, S>newBuilder(entityId, event, version).setDate(timestamp).build();
        return ei;

      } else {
        return null;
      }
    } catch (Exception e) {
      return null;
    }
  }
}
