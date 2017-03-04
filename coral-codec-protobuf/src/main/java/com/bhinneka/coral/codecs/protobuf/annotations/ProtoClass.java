package com.bhinneka.coral.codecs.protobuf.annotations;

import com.google.protobuf.Message;
import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ProtoClass {
  Class<? extends Message> value();
}

