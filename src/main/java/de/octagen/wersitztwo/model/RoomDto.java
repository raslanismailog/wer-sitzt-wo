package de.octagen.wersitztwo.model;

import de.octagen.wersitztwo.model.PersonDto;
import lombok.Data;

import java.io.Serializable;
import java.util.Set;


@Data
public class RoomDto implements Serializable {
    private  Integer room;
    private  Set<PersonDto> people;

}
