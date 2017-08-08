package com.pat.demo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@Slf4j
@RunWith(SpringRunner.class)
@SuppressWarnings("UnnecessaryLocalVariable")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class DemoWebTest {

    private Faker faker = new Faker();

    @Resource
    private TestRestTemplate restTemplate;

    @Resource
    private ObjectMapper objectMapper;

    @Before
    public void setUp() throws Exception {
        configureObjectMapper();
        configureRestTemplate();
    }

    public void configureObjectMapper() throws Exception {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public void configureRestTemplate() {

        final HttpComponentsClientHttpRequestFactory requestFactory =
                new HttpComponentsClientHttpRequestFactory();

        restTemplate.getRestTemplate().setRequestFactory(requestFactory);
    }

    @Test
    public void testPatch() throws Exception {

        final Person person = this.restTemplate.postForObject("/person",
                generateRandomPerson(), Person.class
        );

        log.info("Created person {}", person);

        assertNotNull(person);

        final String newLastName = faker.name().lastName();
        final Person patchUpdate = Person.builder().lastName(newLastName).build();

        log.info("Patching last name to {}", newLastName);

        final Person updatedPerson =
                restTemplate.patchForObject("/person/{id}", patchUpdate, Person.class, person.getId());

        log.info("Updated person: {}", updatedPerson);

        final Person expectedPerson = person;
        person.setLastName(newLastName);

        assertEquals(expectedPerson, updatedPerson);
    }

    @Test
    public void testPathWithEmptyString_nulls_value() throws Exception {

        final Person person = this.restTemplate.postForObject("/person",
                generateRandomPerson(), Person.class
        );

        log.info("Created person {}", person);

        assertNotNull(person);

        final Person patchUpdate = Person.builder().lastName("").build();

        log.info("Nulling out last name");

        final Person updatedPerson =
                restTemplate.patchForObject("/person/{id}", patchUpdate, Person.class, person.getId());

        log.info("Updated person: {}", updatedPerson);

        final Person expectedPerson = person;
        expectedPerson.setLastName(null);

        assertEquals(expectedPerson, updatedPerson);
    }

    @Test
    public void testPatchWith_NestedChildren() throws Exception {

        final Person person = this.restTemplate.postForObject("/person",
                generateRandomPerson(), Person.class
        );

        log.info("Created person {}", person);

        assertNotNull(person);

        final Person child = generateRandomPerson();
        final Person patchUpdate = Person.builder().children(Collections.singletonList(child)).build();

        log.info("Adding a child {}", child);

        final Person updatedPerson =
                restTemplate.patchForObject("/person/{id}", patchUpdate, Person.class, person.getId());

        log.info("Updated person: {}", updatedPerson);

        final Person expectedPerson = person;
        person.getChildren().add(child);

        assertEquals(expectedPerson, updatedPerson);
    }

    public Person generateRandomPerson() {
        return Person.builder()
                .firstName(faker.name().firstName())
                .lastName(faker.name().lastName())
                .id("1")
                .build();

    }
}
