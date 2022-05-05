package de.octagen.wersitztwo.model;

import de.octagen.wersitztwo.model.Person;
import lombok.*;
import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "room")
@NoArgsConstructor @AllArgsConstructor
public class Room {

    @Id
    @Column(name = "room_id", nullable = false)
    private Integer room;

    @OneToMany(targetEntity = Person.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "room_id", referencedColumnName = "room_id")
    private List<Person> people;

    public Room(Integer id) {
        this.room = id;
    }

}