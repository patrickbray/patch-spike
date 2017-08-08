package com.pat.demo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
class Person {

    private String id;
    private String firstName;
    private String lastName;

}
