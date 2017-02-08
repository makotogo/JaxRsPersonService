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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import com.makotojava.learn.jaxrs.dao.PersonFinder;
import com.makotojava.learn.jaxrs.model.Person;
import com.makotojava.learn.jaxrs.model.Person.EyeColor;
import com.makotojava.learn.jaxrs.model.Person.Gender;

/**
 * JUnit test class for testing the Repository class.
 * 
 * @author sperry
 *
 */
public class RepositoryTest {

  private static final Logger log = Logger.getLogger(RepositoryTest.class);

  private Repository repository;
  private PersonFinder personFinder;

  @Before
  public void setUp() throws Exception {
    repository = Repository.instance();
    personFinder = repository;
  }

  @Test
  public void testFindAll() {
    log.info("*** BEGIN TEST ***");
    //
    // Get the repository size
    long repositorySize = repository.getRepositorySize();
    //
    // Retrieve all Person objects in the Repository
    List<Person> results = personFinder.findAll();
    //
    // Make sure the size reported matches the size
    /// of the collection we get back.
    assertEquals(repositorySize, results.size());
    //
    // Dump out the results (if log debug enabled)
    if (log.isDebugEnabled()) {
      for (Person person : results) {
        log.debug(ReflectionToStringBuilder.toString(person));
      }
    }
    log.info("*** END TEST ***");
  }

  @Test
  public void testFindAllByLastName() {
    log.info("*** BEGIN TEST ***");
    //
    // Retrieve all objects from the Repository
    List<Person> persons = personFinder.findAll();
    //
    // Make sure we get something back (the collection should never
    /// be empty)
    assertFalse(persons.isEmpty());
    //
    // Grab the first object and search for it by last name (it's possible
    /// there are multiple Person objects with the same last name)
    Person personToSearch = persons.get(0);
    List<Person> results = personFinder.findAllByLastName(personToSearch.getLastName());
    //
    // Make sure we get something back (there should be at least one result)
    assertFalse(results.isEmpty());
    //
    // Compare the last names. They should match.
    Person resultPerson = results.get(0);
    assertEquals(personToSearch.getLastName(), resultPerson.getLastName());
    log.info("*** END TEST ***");
  }

  @Test
  public void testFindById() {
    log.info("*** BEGIN TEST ***");
    //
    // Do the Find by specific ID
    Person personById = personFinder.findById(1L);
    //
    // Make sure we get something back (ID == 1 should definitely be valid)
    assertNotNull(personById);
    //
    // Add a new Person to the Repository
    Person personToAdd = new Person("lastName", "firstName", 100, EyeColor.UNKNOWN, Gender.UNKNOWN);
    personToAdd = personFinder.addPerson(personToAdd);
    //
    // Make sure the Person was added
    assertNotNull(personToAdd);
    //
    // Get the ID
    Long id = personToAdd.getId();
    //
    // Find by that ID
    personById = personFinder.findById(id);
    //
    // Make sure we got something back
    assertNotNull(personById);
    //
    // Make sure it's the Person we expected
    assertEquals(id, personById.getId());
    log.info("*** END TEST ***");
  }

  @Test
  public void testAddPerson() {
    log.info("*** BEGIN TEST ***");
    Person person = PersonGenerator.createPerson();
    // We need to know
    long repositorySizeOriginal = repository.getRepositorySize();
    Person personAdded = personFinder.addPerson(person);
    //
    // The Person object that was added should have these attributes
    /// set after adding to the Repository
    assertNotNull(personAdded.getId());
    assertNotNull(personAdded.getWhenCreated());
    //
    // The object that was added should have identical attribute
    /// values as the original.
    assertEquals(person.getAge(), personAdded.getAge());
    assertEquals(person.getEyeColor(), personAdded.getEyeColor());
    assertEquals(person.getFirstName(), personAdded.getFirstName());
    assertEquals(person.getGender(), personAdded.getGender());
    assertEquals(person.getLastName(), personAdded.getLastName());
    //
    // Get the Repository size
    long repositorySizeCurrent = repository.getRepositorySize();
    //
    // Make sure the person object got added
    assertEquals(repositorySizeOriginal + 1, repositorySizeCurrent);
    log.info("*** END TEST ***");
  }

  @Test
  public void testUpdatePerson() {
    log.info("*** BEGIN TEST ***");
    //
    // Create a new Person object
    Person person = PersonGenerator.createPerson();
    //
    // Add that Person (or they won't have an ID, and an "update"
    /// makes no sense)
    person = personFinder.addPerson(person);
    //
    // Modify their last name
    String lastNameOriginal = person.getLastName();
    String lastNameModified = lastNameOriginal + "MOD";
    person.setLastName(lastNameModified);
    //
    // Update in the Repository
    Person personModified = personFinder.updatePerson(person);
    //
    // Make sure the IDs match
    assertEquals(person.getId(), personModified.getId());
    //
    // Make sure the Person was actually modified
    assertEquals(lastNameModified, personModified.getLastName());
    //
    // Not convinced? Let's grab it from the Repository and check it.
    Person personFromDb = personFinder.findById(personModified.getId());
    assertEquals(lastNameModified, personFromDb.getLastName());
    log.info("*** END TEST ***");
  }

}
