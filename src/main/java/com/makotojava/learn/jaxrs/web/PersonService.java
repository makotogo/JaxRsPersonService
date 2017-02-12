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
package com.makotojava.learn.jaxrs.web;

import java.net.URI;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.makotojava.learn.jaxrs.dao.PersonDao;
import com.makotojava.learn.jaxrs.dao.PersonFinder;
import com.makotojava.learn.jaxrs.model.Person;
import com.makotojava.learn.jaxrs.util.Repository;

@Path(PersonService.PATH_PERSON_SERVICE)
public class PersonService {

  private static final Logger log = Logger.getLogger(PersonService.class);

  public static final String PATH_PERSON_SERVICE = "/PersonService";
  public static final String PATH_FIND_ALL = "/FindAll";
  public static final String PATH_FIND_BY_ID = "/FindById";
  public static final String PATH_FIND_BY_LAST_NAME = "/FindAllByLastName";
  public static final String PATH_ADD_PERSON = "/AddPerson";
  public static final String PATH_UPDATE_PERSON = "/UpdatePerson";
  public static final String PATH_DELETE_PERSON = "/DeletePerson";

  public static final String PATH_PARAM_ID = "/{Id}";
  public static final String PATH_PARAM_LAST_NAME = "/{LastName}";

  private PersonDao personDao;

  private PersonFinder getPersonFinder() {
    if (personDao == null) {
      personDao = Repository.instance();
    }
    return personDao;
  }

  private PersonDao getPersonDao() {
    if (personDao == null) {
      personDao = Repository.instance();
    }
    return personDao;
  }

  @GET
  @Path(PATH_FIND_ALL)
  @Produces(MediaType.APPLICATION_JSON)
  public Response findAll() {
    Response ret = null;

    log.info("Executingmethod: " + PATH_FIND_ALL);
    List<Person> persons;
    //
    // Call findAll on the PersonFinder
    persons = getPersonFinder().findAll();
    if (!persons.isEmpty()) {
      ret = Response.ok().entity(persons).build();
    } else {
      ret = Response.status(404).entity("Repository appears to be empty.").build();
    }

    ret = Response.ok(persons).build();

    prettyPrintObject(ret);

    return ret;
  }

  @GET
  @Path(PATH_FIND_BY_ID + PATH_PARAM_ID)
  @Produces(MediaType.APPLICATION_JSON)
  public Response findById(@PathParam("Id") Long id) {
    Response ret = null;

    log.info("Executing method: " + PATH_FIND_BY_ID + "/{" + id + "}");
    Person person;
    //
    // Call findById on the PersonFinder
    person = getPersonFinder().findById(id);
    if (person != null) {
      ret = Response.ok(person).build();
    } else {
      ret = Response.status(404).entity("Could not locate person by ID " + id + " in the repository.").build();
    }
    prettyPrintObject(ret);

    return ret;
  }

  @GET
  @Path(PATH_FIND_BY_LAST_NAME + PATH_PARAM_LAST_NAME)
  @Produces(MediaType.APPLICATION_JSON)
  public Response findAllByLastName(@PathParam("LastName") String lastName) {
    Response ret = null;

    log.info("Executing method: " + PATH_FIND_BY_LAST_NAME + "/{" + lastName + "}");
    List<Person> persons;
    //
    // Call the findAllByLastName method on PersonFinder
    persons = getPersonFinder().findAllByLastName(lastName);
    if (!persons.isEmpty()) {
      ret = Response.ok().entity(persons).build();
      prettyPrintObject(persons);
    } else {
      ret = Response.status(404).entity("No Person could be located by last name '" + lastName + "' in the repository.")
          .build();
    }
    prettyPrintObject(ret);

    return ret;
  }

  @PUT
  @Path(PATH_ADD_PERSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response addPerson(Person personToAdd) {
    Response ret = null;
    //
    // Add the Person to the Repository
    Person personAdded = getPersonDao().addPerson(personToAdd);
    if (personAdded != null) {
      //
      // Create response
      URI addPersonURI = URI.create(PATH_PERSON_SERVICE + PATH_ADD_PERSON);
      ret = Response.created(addPersonURI).entity(personAdded).build();
    } else {
      ret = Response.status(400).entity("Person could not added, please correct error(s) and try again.").build();
    }
    prettyPrintObject(ret);

    return ret;
  }

  @POST
  @Path(PATH_UPDATE_PERSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response updatePerson(Person personToUpdate) {
    Response ret = null;
    //
    // Update the Person to the Repository
    Person personUpdated = getPersonDao().updatePerson(personToUpdate);
    if (personUpdated != null) {
      ret = Response.ok(personUpdated).build();
    } else {
      ret = Response.status(400).entity("Person could not be updated. Please correct error(s) and try again.").build();
    }
    //
    // Create response
    URI updatePersonURI = URI.create(PATH_PERSON_SERVICE + PATH_UPDATE_PERSON);
    ret = Response.ok(updatePersonURI).entity(personUpdated).build();
    return ret;
  }

  @DELETE
  @Path(PATH_DELETE_PERSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response deletePerson(Person personToDelete) {
    Response ret = null;
    //
    // Delete the Person to the Repository
    Person personDeleted = getPersonDao().deletePerson(personToDelete);
    if (personDeleted != null) {
      ret = Response.noContent().build();
    } else {
      ret = Response.status(400).entity("Person could not be deleted. Please correct error(s) and try again.").build();
    }
    //
    // Create response
    URI deletePersonURI = URI.create(PATH_PERSON_SERVICE + PATH_DELETE_PERSON);
    ret = Response.ok(deletePersonURI).entity(personDeleted).build();
    return ret;
  }

  /**
   * Pretty prints the JSON object to the log.
   * 
   * @param object
   */
  private void prettyPrintObject(Object object) {
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      log.debug("Service(s) provided: \n" +
          objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object));
    } catch (JsonProcessingException e) {
      log.error("JSON Processing Exception Occurred: ", e);
    }
  }
}
