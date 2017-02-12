/*
 * Copyright 2017 Makoto Consulting Group, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.makotojava.learn.jaxrs.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.makotojava.learn.jaxrs.dao.PersonDao;
import com.makotojava.learn.jaxrs.model.Person;

/**
 * Represents a data store. Yeah, I know, it's not a real data store.
 * It's sample code.
 * 
 * @author sperry
 *
 */
public class Repository implements PersonDao {

  private Repository() {
  }

  private static Repository instance;

  /**
   * The one and only way to get an instance of this class.
   * 
   * @return
   */
  public static Repository instance() {
    if (instance == null) {
      instance = new Repository();
      // Create 10 Person objects and add them to the Repository/DB
      for (int aa = 0; aa < 10; aa++) {
        instance.addPerson(PersonGenerator.createPerson());
      }
    }
    return instance;
  }

  /**
   * The Database. Primitive, like us humans.
   */
  private static final List<Person> DATA = new ArrayList<Person>();

  /**
   * Returns the size of the Repository, i.e., the number of Person
   * objects it contains.
   * 
   * @return long - The number of Person objects in the Repository
   */
  public long getRepositorySize() {
    return DATA.size();
  }

  @Override
  public List<Person> findAll() {
    List<Person> ret = new ArrayList<>();
    ret.addAll(DATA);
    return ret;
  }

  @Override
  public List<Person> findAllByLastName(String lastName) {
    List<Person> ret = new ArrayList<>();
    for (Person person : DATA) {
      // Case-insensitive "search"
      if (person.getLastName().equalsIgnoreCase(lastName)) {
        ret.add(person);
      }
    }
    return ret;
  }

  @Override
  public Person findById(Long id) {
    Person ret = null;
    for (Person person : DATA) {
      if (person.getId().equals(id)) {
        ret = person;
        break;
      }
    }
    return ret;
  }

  @Override
  public Person addPerson(Person person) {
    Person ret = null;
    long nextId = DATA.size() + 1;
    Date now = new Date();
    // Make a deep copy, even for sample code it just seems like the right call
    Person personForDb = new Person(person.getLastName(), person.getFirstName(), person.getAge(), person.getEyeColor(),
        person.getGender());
    personForDb.setId(nextId);
    personForDb.setWhenCreated(now);
    // Add the person
    if (DATA.add(personForDb)) {
      ret = personForDb;
    }
    return ret;
  }

  @Override
  public Person updatePerson(Person person) {
    Person ret = null;
    Person personFromDb = findById(person.getId());
    if (personFromDb != null) {
      // Naive field-by-field copy.
      personFromDb.setAge(person.getAge());
      personFromDb.setEyeColor(person.getEyeColor());
      personFromDb.setFirstName(person.getFirstName());
      personFromDb.setGender(person.getGender());
      personFromDb.setLastName(person.getLastName());
      ret = personFromDb;
    }
    return ret;
  }

  @Override
  public Person deletePerson(Person person) {
    // TODO Auto-generated method stub
    return null;
  }

}
