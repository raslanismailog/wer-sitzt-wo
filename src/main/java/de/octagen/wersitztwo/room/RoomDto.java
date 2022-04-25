package de.octagen.wersitztwo.room;

import de.octagen.wersitztwo.person.PersonDto;
import lombok.Data;

import java.io.Serializable;
import java.util.Set;


@Data
public class RoomDto implements Serializable {
    private  Integer room;
    private  Set<PersonDto> people;

}
