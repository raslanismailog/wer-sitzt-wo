package de.octagen.wersitztwo;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;

@Data
@Service
public class RoomService {

    private final RoomRepository roomRepo;
    private final PersonRepository personRepo;
    private final List<String> nameAdditions = new ArrayList<>(Arrays.asList("van", "von", "de"));
    private final String filePath = "./sitzplan2.csv";
    private final String newFilePath = "./newsitzplan2.csv";

    public RoomService(RoomRepository roomRepo, PersonRepository personRepo) {
        this.roomRepo = roomRepo;
        this.personRepo = personRepo;
    }

    public void formatCsvFile(String filePath, String newfilePath) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        PrintWriter printWriter = new PrintWriter(new FileWriter(newfilePath));
        String line;
        int count = 0;

        while ((line = br.readLine()) != null) {
            if (!Character.isDigit(line.charAt(0))) {
                printWriter.print(" " + line);
            } else {
                if (count == 0) {
                    printWriter.print(line);
                } else {
                    printWriter.println("");
                    printWriter.print(line);
                }
            }
            count++;
        }
        printWriter.close();
    }

    public List<String> csvFileToList(String filePath) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        List<String> data = new ArrayList<>();
        String line;
        int count = 0;

        while ((line = br.readLine()) != null) {
            if (!Character.isDigit(line.charAt(0))) {
                data.set(data.size()-1, data.get(data.size()-1) + " " + line);
            } else {
                    data.add(line);
            }
            count++;
        }
        data.forEach(System.out::println);
        return data;
    }



    public Room getOneRoom(int roomNr) throws IOException {
        return this.getAllRooms2().stream()
                .filter(room -> room.getRoom() == roomNr)
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("This room doesn't exist"));
    }



    public void addRoomsToDb(List<Room> rooms) {
        roomRepo.saveAll(rooms);
        rooms.forEach(room -> {
            room.getPeople().forEach(person -> person.setRoom(room));
            personRepo.saveAll(room.getPeople());
        });
    }

    public List<RoomDto> convertToDto(List<Room> rooms) {
        List<RoomDto> roomDtos = new ArrayList<>();

        rooms.forEach(room -> {

            RoomDto roomDto = new RoomDto();
            BeanUtils.copyProperties(room, roomDto);
            roomDtos.add(roomDto);
            Set<PersonDto> people = new LinkedHashSet<>();

            room.getPeople().forEach(person -> {
                PersonDto personDto = new PersonDto();

                BeanUtils.copyProperties(person, personDto);
                people.add(personDto);
            });

            roomDto.setPeople(people);
        });

        return roomDtos;
    }

    public List<Room> getAllRooms() throws IOException {
        formatCsvFile(this.filePath, newFilePath);
        BufferedReader br = new BufferedReader(new FileReader(this.newFilePath));
        List<Room> rooms = new ArrayList<>();
        br.lines().forEach(s -> {
            if (!s.isEmpty()) {
                String[] record = s.trim().split(",");
                Room room = new Room(Integer.parseInt(record[0]));
                Set<Person> people = new HashSet<>();
                for (int i = 1; i < record.length; i++) {
                    Person person = new Person();
                    if (!record[i].isEmpty()) {
                        List<String> token = new ArrayList<>(Arrays.stream(record[i].trim().split(" ")).toList());
                        System.out.println(token);
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

    public List<Room> getAllRooms2() throws IOException {
        List<String> data= csvFileToList(this.filePath);
        List<Room> rooms = new ArrayList<>();
        System.out.println("Hier");
        data.forEach(System.out::println);
        System.out.println("to hier");
        data.forEach(s -> {
            if (!s.isEmpty()) {
                String[] record = s.trim().split(",");
                Room room = new Room(Integer.parseInt(record[0]));
                Set<Person> people = new HashSet<>();
                for (int i = 1; i < record.length; i++) {
                    Person person = new Person();
                    if (!record[i].isEmpty()) {
                        List<String> token = new ArrayList<>(Arrays.stream(record[i].trim().split(" ")).toList());
                        System.out.println(token);
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

        if (token.size() > 0) {
            if (token.get(0).equals("Dr.")) {
                person.setTitle(token.get(0));
                token.remove(0);
            } else {
                person.setTitle("");
            }
            this.nameAdditions.forEach(na -> {
                if (token.contains(na)) {
                    person.setNameAddition(na);
                    token.remove(na);
                } else {
                    person.setNameAddition("");
                }
                person.setFirstName(token.get(0));
                person.setLastName(token.get(1));
            });

            if (token.size() > 2 && token.get(2).startsWith("(")) {
                person.setLdapUser(token.get(2).substring(1, token.get(2).length() - 1));
            } else if (token.size() > 3 && token.get(3).startsWith("(")) {
                person.setLastName(token.get(1) + " " + token.get(2));
                person.setLdapUser(token.get(3).substring(1, token.get(3).length() - 1));
            }

        }
    }

}
