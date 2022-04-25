package de.octagen.wersitztwo.room;

import de.octagen.wersitztwo.person.Person;
import de.octagen.wersitztwo.person.PersonDto;
import de.octagen.wersitztwo.person.PersonRepository;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        List<String> data = new ArrayList<>();
        String line;

        while ((line = br.readLine()) != null) {
            if (!Character.isDigit(line.charAt(0))) {
                data.set(data.size() - 1, data.get(data.size() - 1) + " " + line);
            } else {
                data.add(line);
            }
        }

        return data;
    }

    public List<Room> addRoomsToDb(List<Room> rooms) {
        roomRepo.saveAll(rooms);

        rooms.forEach(room -> {
            room.getPeople().forEach(person -> person.setRoom(room));
            personRepo.saveAll(room.getPeople());
        });

        return rooms;
    }

    public List<RoomDto> convertToDto(List<Room> rooms) {
        List<RoomDto> roomDtos = new ArrayList<>();

        rooms.forEach(room -> {
            roomDtos.add(this.getDto(room));
        });

        return roomDtos;
    }

    public RoomDto getDto(Room room) {
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
        data.forEach(s -> {
            if (!s.isEmpty()) {
                String[] record = s.trim().split(",");
                Room room = new Room(Integer.parseInt(record[0]));
                Set<Person> people = new HashSet<>();
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

        if (!token.isEmpty() && token.contains("Dr.")) {
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
