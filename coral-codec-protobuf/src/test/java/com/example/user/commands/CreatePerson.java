package com.example.user.commands;

import com.bhinneka.coral.codecs.protobuf.annotations.ProtoClass;
import com.bhinneka.coral.core.Command;
import com.bhinneka.coral.protocols.test.PersonProtocol;
import com.example.user.Person;

import javax.annotation.Nonnull;

@ProtoClass(PersonProtocol.CreatePerson.class)
public final class CreatePerson implements Command<Person> {
  private @Nonnull String name;
  private float age;

  public String getName() {
    return name;
  }

  public float getAge() {
    return age;
  }

  public CreatePerson(@Nonnull String name, float age) {
    this.name = name;
    this.age = age;
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) return true;
    if (other == null || getClass() != other.getClass()) return false;

    CreatePerson that = (CreatePerson) other;

    if (Float.compare(that.age, age) != 0) return false;
    return name.equals(that.name);
  }

  @Override
  public int hashCode() {
    int result = name.hashCode();
    result = 31 * result + (age != +0.0f ? Float.floatToIntBits(age) : 0);
    return result;
  }
}
