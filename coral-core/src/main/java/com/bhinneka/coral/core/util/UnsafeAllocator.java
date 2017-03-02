package com.bhinneka.coral.core.util;


import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import javax.annotation.Nonnull;

/**
 * Class with unsafe operations, please use sparingly. This class will
 * allocate objects without using their constructor
 *
 * @author Didiet Noor
 */
public abstract class UnsafeAllocator {
  /**
   * Method to <b>Unsafely</b> create a object of type {@link T} of class klass.
   *
   * @param klass The class type of object to be allocated
   * @param <T> The type of object to be allocated
   * @return an instance of newly allocated object of type {@link T}
   * @throws Exception any exception occured during allocation
   */
  public abstract <T> T newInstance(Class<T> klass) throws Exception;

  private static void assertInstantiable(Class<?> klass) {
    final int modifiers = klass.getModifiers();

    if (Modifier.isInterface(modifiers)) {
      throw new UnsupportedOperationException("Interface cannot be instantiated: "
          + klass.getName());
    }
    if (Modifier.isAbstract(modifiers)) {
      throw new UnsupportedOperationException("Abstract class cannot be instantiated: "
          + klass.getName());
    }
  }

  /**
   * Create an UnsafeAllocator.
   *
   * @return an instance of {@link UnsafeAllocator}
   */
  public static @Nonnull UnsafeAllocator create() {
    // this one for JVM
    try {
      final Class<?> unsafeClass = Class.forName("sun.misc.Unsafe");
      final Field f = unsafeClass.getDeclaredField("theUnsafe");
      f.setAccessible(true);
      final Object unsafe = f.get(null);
      final Method allocateInstance = unsafeClass.getMethod("allocateInstance", Class.class);
      return new UnsafeAllocator() {
        @Override
        @SuppressWarnings("unchecked")
        public <T> T newInstance(Class<T> c) throws Exception {
          UnsafeAllocator.assertInstantiable(c);
          return (T) allocateInstance.invoke(unsafe, c);
        }
      };
    } catch (Exception ignored) {
      // Ignoramus
    }

    // This is for dalvik for gingerbread
    try {
      final Method getConstructorId = ObjectStreamClass.class
          .getDeclaredMethod("getConstructorId", Class.class);
      getConstructorId.setAccessible(true);
      final int constructorId = (Integer) getConstructorId.invoke(null, Object.class);
      final Method newInstance = ObjectStreamClass.class
          .getDeclaredMethod("newInstance", Class.class, int.class);
      newInstance.setAccessible(true);
      return new UnsafeAllocator() {
        @Override
        @SuppressWarnings("unchecked")
        public <T> T newInstance(Class<T> c) throws Exception {
          UnsafeAllocator.assertInstantiable(c);
          return (T) newInstance.invoke(null, c, constructorId);
        }
      };
    } catch (Exception ignored) {
      // Ignoramus
    }

    // This is for post gingerbread
    try {
      final Method newInstance = ObjectInputStream.class
          .getDeclaredMethod("newInstance", Class.class, Class.class);
      newInstance.setAccessible(true);
      return new UnsafeAllocator() {
        @Override
        @SuppressWarnings("unchecked")
        public <T> T newInstance(Class<T> c) throws Exception {
          UnsafeAllocator.assertInstantiable(c);
          return (T) newInstance.invoke(null, c, Object.class);
        }
      };
    } catch (Exception ignored) {
      // Ignoramus
    }

    return new UnsafeAllocator() {
      @Override
      public <T> T newInstance(Class<T> c) throws Exception {
        throw new UnsupportedOperationException("Cannot allocate" + c);
      }
    };
  }
}