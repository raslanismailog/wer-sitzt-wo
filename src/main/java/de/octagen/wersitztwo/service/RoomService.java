package de.octagen.wersitztwo.service;

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
public class RoomService {

    private final RoomRepository roomRepo;
    private final PersonRepository personRepo;
    private final List<String> nameAdditions = new ArrayList<>(Arrays.asList("van", "von", "de"));

    public RoomService(RoomRepository roomRepo, PersonRepository personRepo) {
        this.roomRepo = roomRepo;
        this.personRepo = personRepo;
    }

    public List<String> csvFileToList(String filePath) throws IOException {
        List<String> data = new ArrayList<>();

        Path path = Paths.get(filePath);
        String fileName = new File(String.valueOf(path)).getName();
        if (Files.exists(path) && fileName.endsWith("csv")) {
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            data.addAll(br.lines().toList());
        }

        return data;
    }

    public List<Room> addRoomsToDb(List<Room> rooms) {

        if (rooms != null && !rooms.isEmpty()) {

            roomRepo.saveAll(rooms);

            rooms.stream().filter(room -> room != null && !room.getPeople().isEmpty()).forEach(room -> {
                try {
                    personRepo.saveAll(room.getPeople());
                } catch (NullPointerException e) {
                    System.out.println(e.getMessage());
                }
            });

        }

        return rooms;
    }


    public List<RoomDto> convertToDto(List<Room> rooms) {
        List<RoomDto> roomDtos = new ArrayList<>();

        rooms.forEach(room -> {
            roomDtos.add(this.getRoomDto(room));
        });

        return roomDtos;
    }

    public RoomDto getRoomDto(Room room) {
        RoomDto roomDto = new RoomDto();
        BeanUtils.copyProperties(room, roomDto);
        Set<PersonDto> people = new LinkedHashSet<>();

        room.getPeople().forEach(person -> {
            PersonDto personDto = new PersonDto();
            BeanUtils.copyProperties(person, personDto);
            people.add(personDto);
        });

        roomDto.setPeople(people);
        return roomDto;
    }

    public List<Room> getAllRooms(String filePath) throws IOException {
        List<String> data = csvFileToList(filePath);
        List<Room> rooms = new ArrayList<>();
        data.forEach(s -> { //TODO: rename
            if (!s.isEmpty()) {
                String[] record = s.trim().split(",");
                Room room = new Room(Integer.parseInt(record[0]));
                List<Room> rooms1 = data.stream().map(line -> new Room(Integer.parseInt(Arrays.stream(line.trim().split(",")).toList().get(0)))).toList();
                List <Person> people = new LinkedList<>();

                for (int i = 1; i < record.length; i++) {
                    Person person = new Person();
                    if (!record[i].isEmpty()) {
                        List<String> token = new ArrayList<>(Arrays.stream(record[i].trim().split(" ")).toList());
                        this.setPerson(token, person);
                        people.add(person);
                    }
                    room.setPeople(people);
                }
                rooms.add(room);
            }
        });
        return rooms;
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

            token.forEach(s -> {
                        if (s.matches("^\\(.*\\)$")) {
                            person.setLdapUser(s.substring(1, s.length() - 1));
                        }
                    }
            );
        }


    }


}
