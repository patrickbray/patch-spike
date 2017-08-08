package com.pat.demo;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;

import java.util.List;

@Data
@Builder
class Person {

    private String id;
    private String firstName;
    private String lastName;

    @Singular
    private List<Person> children;

}
