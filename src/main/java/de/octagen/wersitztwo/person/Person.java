package de.octagen.wersitztwo.person;

import de.octagen.wersitztwo.room.Room;
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
    private Integer id;

    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName = "";

    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName = "";

    @Column(name = "title",nullable = false, length = 30)
    private String title = "";

    @Column(name = "name_addition", length = 30)
    private String nameAddition = "";

    @Column(name = "ldap_user", length = 60)
    private String ldapUser = "";

    @ManyToOne
    @JoinColumn(name = "room_id", referencedColumnName = "room_id", nullable = false)
    private Room room;

}