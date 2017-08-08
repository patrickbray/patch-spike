package com.pat.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/person")
public class PersonRest {

    private Person person;

    @Resource
    private ObjectMapper objectMapper;

    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
    public Person partialUpdatePerson(HttpServletRequest servletRequest) {

        try {

            final Person updatedPerson = this.objectMapper.readerForUpdating(this.person).readValue(
                    new ServletServerHttpRequest(servletRequest).getBody());

            this.person = updatedPerson;
            return updatedPerson;

        } catch (IOException ex) {
            throw new HttpMessageNotReadableException("Could not read document: " + ex.getMessage(), ex);
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    public Person createPerson(@RequestBody Person person) {

        log.info("Creating person {}", person);

        this.person = person;
        return this.person;
    }
}
