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

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.makotojava.learn.jaxrs.dao.PersonFinder;
import com.makotojava.learn.jaxrs.model.Person;
import com.makotojava.learn.jaxrs.util.Repository;

@Path("/PersonService")
public class PersonService {

  private static final Logger log = Logger.getLogger(PersonService.class);

  private PersonFinder personFinder = Repository.instance();

  @GET
  @Path("/FindAll")
  @Produces(MediaType.APPLICATION_JSON)
  public List<Person> findAll() {
    log.info("Executingmethod: /FindAll");
    List<Person> ret;

    ret = personFinder.findAll();

    prettyPrintObject(ret);

    return ret;
  }

  @GET
  @Path("/FindById/{Id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Person findById(@PathParam("Id") Long id) {
    log.info("Executing method: /FindById/{" + id + "}");
    Person ret;

    ret = personFinder.findById(id);

    prettyPrintObject(ret);

    return ret;
  }

  @GET
  @Path("/FindAllByLastName/{LastName}")
  @Produces(MediaType.APPLICATION_JSON)
  public List<Person> findAllByLastName(@PathParam("LastName") String lastName) {
    log.info("Executing method: /FindAllByLastName/{" + lastName + "}");
    List<Person> ret;

    ret = personFinder.findAllByLastName(lastName);

    prettyPrintObject(ret);

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
