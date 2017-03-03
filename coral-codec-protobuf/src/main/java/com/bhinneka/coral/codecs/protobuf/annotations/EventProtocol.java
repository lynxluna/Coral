package com.bhinneka.coral.codecs.protobuf.annotations;


import com.bhinneka.coral.core.Event;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface EventProtocol {
  Class<? extends Event<?>> value();
}

