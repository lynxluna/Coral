package com.example.user;

import javax.annotation.Nonnull;

public final class Person {
  private @Nonnull String name;
  private float  age;

  public Person(@Nonnull String name, float age) {
    this.name = name;
    this.age = age;
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) return true;
    if (other == null || getClass() != other.getClass()) return false;

    Person person = (Person) other;

    if (Float.compare(person.age, age) != 0) return false;
    return name.equals(person.name);
  }

  @Override
  public int hashCode() {
    int result = name.hashCode();
    result = 31 * result + (age != +0.0f ? Float.floatToIntBits(age) : 0);
    return result;
  }

  @Nonnull
  public String getName() {
    return name;
  }

  public float getAge() {
    return age;
  }
}
