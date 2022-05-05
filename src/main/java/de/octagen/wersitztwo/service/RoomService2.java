package de.octagen.wersitztwo.service;

import de.octagen.wersitztwo.controller.RoomController;
import de.octagen.wersitztwo.model.Person;
import de.octagen.wersitztwo.model.PersonDto;
import de.octagen.wersitztwo.model.Room;
import de.octagen.wersitztwo.model.RoomDto;
import de.octagen.wersitztwo.repository.PersonRepository;
import de.octagen.wersitztwo.repository.RoomRepository;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Data
@Service
public class RoomService2 {

    private final List<String> nameAdditions = new ArrayList<>(Arrays.asList("van", "von", "de"));

    private final RoomRepository roomRepository;
    private final PersonRepository personRepository;

    public RoomService2(RoomRepository roomRepository, PersonRepository personRepository) {

        this.roomRepository = roomRepository;
        this.personRepository = personRepository;

    }

    private static LinkedList<String> parseCsvLine(String line) {
        return new LinkedList<>(Arrays.stream(line.split(",")).toList());
    }

    public void addRoomsToDb(List<String> list) {
        List<LinkedList<String>> roomData = list.stream().map(RoomService2::parseCsvLine).toList();
        roomData.forEach(record -> {
            List<Person> people = new ArrayList<>();
            Room room = new Room(Integer.parseInt(record.get(0)));

            record.remove(0);
            record.forEach(personData -> {
                List<String> token = new ArrayList<>(Arrays.stream(personData.trim().split(" ")).toList());
                Person person = new Person();
                this.setPerson(token, person);
                people.add(person);
            });
            room.setPeople(people);
            roomRepository.save(room);

        });

    }

    public void setPerson(List<String> token, Person person) {
        if (!token.isEmpty() && token.size() > 2) {
            if (token.contains("Dr.")) {
                person.setTitle(token.get(0));
                token.remove("Dr.");
            }
            this.nameAdditions.forEach(na -> {
                if (token.contains(na)) {
                    person.setNameAddition(na);
                    token.remove(na);
                }

            });

            person.setFirstName(token.get(0));

            person.setLastName(token.get(1));

            token.forEach(line -> {
                        if (line.matches("^\\(.*\\)$")) {
                            person.setLdapUser(line.substring(1, line.length() - 1));
                        }
                    }
            );
        }

    }
}
