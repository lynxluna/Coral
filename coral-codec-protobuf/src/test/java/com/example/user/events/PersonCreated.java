package com.example.user.events;

import com.bhinneka.coral.codecs.protobuf.annotations.ProtoClass;
import com.bhinneka.coral.core.Event;
import com.bhinneka.coral.protocols.test.PersonProtocol;
import com.example.user.Person;

import javax.annotation.Nonnull;
import java.net.URL;

@ProtoClass(PersonProtocol.PersonCreated.class)
public final class PersonCreated implements Event<Person> {
  private @Nonnull String name;
  private float age;
  private URL website;

  public String getName() {
    return name;
  }

  public float getAge() {
    return age;
  }

  public PersonCreated(@Nonnull String name, float age, URL website) {
    this.name = name;
    this.age = age;
    this.website = website;
  }

  public PersonCreated(@Nonnull String name, float age) {
    this(name, age, null);
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) return true;
    if (other == null || getClass() != other.getClass()) return false;

    PersonCreated that = (PersonCreated) other;

    if (Float.compare(that.age, age) != 0) return false;
    if (!name.equals(that.name)) return false;
    return website != null ? website.equals(that.website) : that.website == null;
  }

  @Override
  public int hashCode() {
    int result = name.hashCode();
    result = 31 * result + (age != +0.0f ? Float.floatToIntBits(age) : 0);
    result = 31 * result + (website != null ? website.hashCode() : 0);
    return result;
  }
}
