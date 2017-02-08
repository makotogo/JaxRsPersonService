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
package com.makotojava.learn.jaxrs.dao;

import java.util.List;

import com.makotojava.learn.jaxrs.model.Person;

/**
 * The interface obeyed by any Repository claiming to find Person objects.
 * 
 * @author sperry
 *
 */
public interface PersonFinder {

  /**
   * Find all Person objects and return the collection to the caller.
   * 
   * @return List<Person> a List of Person objects. Represents all Person
   *         objects in the data store.
   */
  public List<Person> findAll();

  /**
   * Find the Person object by the specified ID.
   * 
   * @param id
   *          The unique ID of the Person object in the Repository
   * 
   * @return Person - the Person object, or null if it could not be found
   *         using the specified ID.
   */
  public Person findById(Long id);

  /**
   * Find all Person objects in the DB with the specified last name.
   * 
   * @param lastName
   * @return
   */
  public List<Person> findAllByLastName(String lastName);

  /**
   * Add the specified Person object to the DB.
   * 
   * @param person
   *          The Person object to add.
   * 
   * @return Person - the Person object just added
   */
  public Person addPerson(Person person);

  /**
   * Updates the specified Person object, if it could be located
   * in the Repository.
   * 
   * @param person
   *          The Person object with the new field value(s)
   * 
   * @return Person - the Person object that was updated or null if the object could not be located.
   */
  public Person updatePerson(Person person);

  /**
   * Deletes the specified Person object, if it exists in the
   * repository.
   * 
   * @param person
   *          The Person object with the new field value(s)
   * 
   * @return Person - the Person object that was updated or null if the object could not be located.
   */
  public Person deletePerson(Person person);
}
