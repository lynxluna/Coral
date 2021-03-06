package com.ykode.coral.core;

import com.ykode.coral.core.generators.UuidGenerator;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;

public class UuidGeneratorTest {

  @Test
  public void testUUIDGenerator() {
    final IdGenerator<UUID> generator = new UuidGenerator();
    assertThat(generator.nextValue()).isInstanceOf(UUID.class);
  }
}
