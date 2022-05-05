package de.octagen.wersitztwo.controller;

import de.octagen.wersitztwo.model.Person;
import de.octagen.wersitztwo.model.Room;
import de.octagen.wersitztwo.model.RoomDto;
import de.octagen.wersitztwo.repository.PersonRepository;
import de.octagen.wersitztwo.repository.RoomRepository;
import de.octagen.wersitztwo.service.RoomService;
import de.octagen.wersitztwo.service.RoomService2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@RestController
@RequestMapping("api")
@Slf4j
public class RoomController {

    private final RoomRepository roomRepository;
    private final PersonRepository personRepository;
    private final RoomService roomService;
    private final RoomService2 roomService2;

    public RoomController(RoomService roomService, RoomService2 roomService2, RoomRepository roomRepository, PersonRepository personRepository) {
        this.roomService = roomService;
        this.roomService2 = roomService2;
        this.roomRepository = roomRepository;
        this.personRepository = personRepository;

    }






    @PostMapping(value = "import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public List<Room> getFile(@RequestParam("file") MultipartFile file) throws IOException {
        String content = new String(file.getBytes());
        BufferedReader bufferedReader = new BufferedReader(new StringReader(content));
        roomService2.addRoomsToDb(bufferedReader.lines().toList());
        return roomRepository.findAll();
    }

    @GetMapping("/room/{id}")
    public RoomDto getOneRoom(@PathVariable String id) {
        return this.roomService.getRoomDto(this.roomRepository
                .findById(Integer.parseInt(id))
                .orElseThrow(() -> new NoSuchElementException("Room not found")));
    }


    @GetMapping("/room")
    public List<Room> getAllRooms(){
        return roomRepository.findAll();
    }


    @GetMapping("/people")
    public void getPeople(){

    }

}
