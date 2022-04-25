package de.octagen.wersitztwo.room;

import de.octagen.wersitztwo.person.Person;
import lombok.*;
import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "room")
@NoArgsConstructor @AllArgsConstructor
public class Room {

    @Id
    @Column(name = "room_id", nullable = false)
    private Integer room;

    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY)
    private Set<Person> people = new LinkedHashSet<>();

    public Room(Integer id) {
        this.room = id;
    }

}