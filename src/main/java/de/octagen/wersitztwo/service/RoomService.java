package de.octagen.wersitztwo.service;

import de.octagen.wersitztwo.exception.DuplicateResidentException;
import de.octagen.wersitztwo.exception.DuplicateRoomException;
import de.octagen.wersitztwo.exception.RoomNumberNotFourDigitsException;
import de.octagen.wersitztwo.model.Person;
import de.octagen.wersitztwo.model.Room;
import de.octagen.wersitztwo.repository.PersonRepository;
import de.octagen.wersitztwo.repository.RoomRepository;
import lombok.Data;
import lombok.Value;
import org.springframework.stereotype.Service;

import javax.management.relation.RoleInfoNotFoundException;
import java.util.*;

@Data
@Service
public class RoomService {

    private final List<String> nameAdditions = new ArrayList<>(Arrays.asList("van", "von", "de"));

    private final RoomRepository roomRepository;
    private final PersonRepository personRepository;

    public RoomService(RoomRepository roomRepository, PersonRepository personRepository) {

        this.roomRepository = roomRepository;
        this.personRepository = personRepository;

    }


    private static LinkedList<String> parseCsvLine(String line) {
        return new LinkedList<>(Arrays.stream(line.split(",")).toList());
    }

    public void addRoomsToDb(List<String> list) {
        List<LinkedList<String>> roomData = list.stream().map(RoomService::parseCsvLine).toList();
        Set<Integer> roomNr = new HashSet<>();
        Set<String> ldapUser = new HashSet<>();
        roomData.forEach(record -> {
            List<Person> people = new ArrayList<>();
            Room room = new Room(Integer.parseInt(record.get(0)));

            if (room.getRoom().toString().length() != 4) {
                System.out.println(room.getRoom().toString().length());
                throw new RoomNumberNotFourDigitsException("Room number: " + record.get(0) + " is not four digits");
            }

            if (!roomNr.add(room.getRoom()) || roomRepository.findAll().contains(room)) {
                throw new DuplicateRoomException("a room with number " +room.getRoom() + " does exists") ;
            }
            record.remove(0);
            record.forEach(personData -> {
                List<String> token = new ArrayList<>(Arrays.stream(personData.trim().split(" ")).toList());
                Person person = new Person();
                this.setPerson(token, person);
                if (!ldapUser.add(person.getLdapUser())) {
                    throw new DuplicateResidentException(person.getLdapUser() + " does exists") ;
                }
                people.add(person);
            });
            room.setPeople(people);
            roomRepository.save(room);

        });

    }


    public void setPerson(List<String> token, Person person) {

        person.setTitle(token.contains( "Dr.") ? "Dr." : "");
        token.remove("Dr.");
        String ldapUser = token.get(token.size()-1);
        person.setLdapUser(ldapUser.contains("(") ? ldapUser.substring(1, ldapUser.length() - 1) : "");
        token.remove(ldapUser);
        String nameAddition = token.stream().filter(this.nameAdditions::contains).findFirst().orElse(null);
        person.setNameAddition(nameAddition != null ? nameAddition : "");
        token.remove(nameAddition);
        person.setLastName(token.get(token.size()-1));
        token.remove(token.get(token.size()-1));
        person.setFirstName(String.join(" ", token));

    }
}
