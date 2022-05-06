package de.octagen.wersitztwo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "person") @NoArgsConstructor @AllArgsConstructor
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    @JsonProperty("id")
    private Integer id;

    @Column(name = "first_name", nullable = false, length = 50)
    @JsonProperty("first name")
    private String firstName = "";

    @Column(name = "last_name", nullable = false, length = 50)
    @JsonProperty("last name")
    private String lastName = "";

    @Column(name = "title",nullable = false, length = 30)
    @JsonProperty("title")
    private String title = "";

    @Column(name = "name_addition", length = 30)
    @JsonProperty("name addition")
    private String nameAddition = "";

    @Column(name = "ldap_user", length = 60)
    @JsonProperty("ldapuser")
    private String ldapUser = "";

}