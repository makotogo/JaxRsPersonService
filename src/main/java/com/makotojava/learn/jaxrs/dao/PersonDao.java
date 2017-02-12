package com.makotojava.learn.jaxrs.dao;

import com.makotojava.learn.jaxrs.model.Person;

public interface PersonDao extends PersonFinder {

  /**
   * Add the specified Person object to the DB.
   * 
   * @param person
   *          The Person object to add.
   * 
   * @return Person - the Person object just added, or null if
   *         there was a problem.
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
