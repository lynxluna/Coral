package io.getcoral.core;

import io.getcoral.core.util.UnsafeAllocator;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;



public class UnsafeAllocatorTest {
  private final UnsafeAllocator allocator = UnsafeAllocator.create();

  private abstract class SomeAbstractClass {}
  private interface SomeInterface {}
  private final class SomePojoClass {
    private final String name;

    // In no way you can instantiate directly
    private SomePojoClass(String name) {
      this.name = name;
    }

    public String getName() {
      return name;
    }
  }

  @Test
  public void testAllocationFailures() {
    assertThatExceptionOfType(UnsupportedOperationException.class)
        .isThrownBy(() -> allocator.newInstance(SomeInterface.class))
        .withMessageContaining("Interface");

    assertThatExceptionOfType(UnsupportedOperationException.class)
        .isThrownBy(() -> allocator.newInstance(SomeAbstractClass.class))
        .withMessageContaining("Abstract");
  }

  @Test
  public void testAllocationPojo() throws Exception {
    assertThat(allocator.newInstance(SomePojoClass.class))
        .isInstanceOf(SomePojoClass.class);
  }
}
