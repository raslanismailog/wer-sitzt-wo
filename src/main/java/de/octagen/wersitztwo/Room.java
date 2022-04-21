package de.octagen.wersitztwo;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@ToString
@Entity
@Table(name = "room")
@NoArgsConstructor @AllArgsConstructor
public class Room {

    @Id
    @Column(name = "room_id", nullable = false)
    private Integer room;

    @OneToMany(mappedBy = "room")
    @ToString.Exclude
    private Set<Person> people = new LinkedHashSet<>();

    public Room(Integer id) {
        this.room = id;
    }

}