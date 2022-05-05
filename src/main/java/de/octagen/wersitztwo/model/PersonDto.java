package de.octagen.wersitztwo.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class PersonDto implements Serializable {
    private String firstName;
    private String lastName;
    private String title;
    private String nameAddition;
    private String ldapUser;
}
