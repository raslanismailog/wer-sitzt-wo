package de.octagen.wersitztwo.controller;

import de.octagen.wersitztwo.exception.RoomNotFountException;
import de.octagen.wersitztwo.model.Room;
import de.octagen.wersitztwo.repository.RoomRepository;
import de.octagen.wersitztwo.service.RoomService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.util.*;

@RestController
@RequestMapping("api")
@Slf4j
public class RoomController {

    private final RoomRepository roomRepository;

    private final RoomService roomService;


    public RoomController(RoomService roomService, RoomRepository roomRepository) {

        this.roomService = roomService;
        this.roomRepository = roomRepository;
    }


    @PostMapping(value = "import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public List<Room> getFile(@RequestParam("file") MultipartFile file) throws IOException {
        String content = new String(file.getBytes());
        BufferedReader bufferedReader = new BufferedReader(new StringReader(content));
        roomService.addRoomsToDb(bufferedReader.lines().toList());

        return roomRepository.findAll();
    }

    @GetMapping("/room/{number}")
    public ResponseEntity<Room> getOneRoom(@PathVariable String number) {

            Room room = roomRepository.findById(Integer.parseInt(number)).orElseThrow(() -> new RoomNotFountException("a room with nummber " + number + " does not exists"));
            return new ResponseEntity<>(room, HttpStatus.OK);
    }

    @GetMapping("/room")
    public List<Room> getAllRooms(){
        return roomRepository.findAll();
    }

}
