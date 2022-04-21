package de.octagen.wersitztwo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Set;


@Data
public class RoomDto implements Serializable {
    private  Integer room;
    private  Set<PersonDto> people;

}
