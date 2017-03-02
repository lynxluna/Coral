package com.bhinneka.coral.core;

import com.bhinneka.coral.core.generators.UuidGenerator;
import org.junit.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class UuidGeneratorTest {

  @Test
  public void testUUIDGenerator() {
    final IdGenerator<UUID> generator = new UuidGenerator();
    assertThat(generator.nextValue()).isInstanceOf(UUID.class);
  }
}
