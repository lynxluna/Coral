package com.bhinneka.coral.core;

import javax.annotation.Nonnull;

// dummy state class
class Person {
  private final String name;
  private final int age;


  public Person(String name, int age) {
    this.name = name; this.age = age;
  }

  public int getAge() {
    return age;
  }

  public String getName() {
    return name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Person person = (Person) o;

    if (age != person.age) return false;
    return name != null ? name.equals(person.name) : person.name == null;
  }

  @Override
  public int hashCode() {
    int result = name != null ? name.hashCode() : 0;
    result = 31 * result + age;
    return result;
  }
}

class CreatePerson implements Command<Person> {
  private final String name;
  private final int age;

  public CreatePerson(String name, int age) {
    this.name = name;
    this.age = age;
  }

  public String getName() {
    return name;
  }

  public int getAge() {
    return age;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    CreatePerson that = (CreatePerson) o;

    if (age != that.age) return false;
    return name.equals(that.name);
  }

  @Override
  public int hashCode() {
    int result = name.hashCode();
    result = 31 * result + age;
    return result;
  }
}

class ChangePersonName implements Command<Person> {
  private final String name;

  public ChangePersonName(@Nonnull String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    ChangePersonName that = (ChangePersonName) o;

    return name.equals(that.name);
  }

  @Override
  public int hashCode() {
    return name.hashCode();
  }
}

class PersonNameChanged implements Event<Person> {
  private final String name;

  public PersonNameChanged(@Nonnull String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    PersonNameChanged that = (PersonNameChanged) o;

    return name.equals(that.name);
  }

  @Override
  public int hashCode() {
    return name.hashCode();
  }
}

class PersonCreated implements Event<Person> {
  private final String name;
  private final int age;

  public PersonCreated(@Nonnull String name, int age) {
    this.name = name;
    this.age = age;
  }

  public String getName() {
    return name;
  }

  public int getAge() {
    return age;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    PersonCreated that = (PersonCreated) o;

    if (age != that.age) return false;
    return name.equals(that.name);
  }

  @Override
  public int hashCode() {
    int result = name.hashCode();
    result = 31 * result + age;
    return result;
  }
}
