package com.ykode.coral.core;

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

class PersonCreated implements Event<Person> {
  private final String name;
  private final int age;

  public PersonCreated(String name, int age) {
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
