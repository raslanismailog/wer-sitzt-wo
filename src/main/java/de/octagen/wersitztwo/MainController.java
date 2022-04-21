package de.octagen.wersitztwo;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("api")
public class MainController {

    private final RoomService roomService;
    public MainController(RoomService roomService) {
        this.roomService = roomService;
    }

    @PostMapping(value = "import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public List<RoomDto> getFile(@RequestParam("file") MultipartFile file) throws IOException {
        Date date = new Date();
        byte[] data = file.getBytes();
        Path path = Paths.get("./"+date.getTime()+"_"+file.getOriginalFilename());
        Files.write(path, data);
        List<Room> rooms = this.roomService.getAllRooms2();
        this.roomService.addRoomsToDb(rooms);
        return this.roomService.convertToDto(rooms);
    }

    @GetMapping("/room/{id}")
    public ResponseEntity<Room> getRoom(@PathVariable String id) {
        Room room;

        try {
             room = this.roomService.getOneRoom(Integer.parseInt(id));
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(room, HttpStatus.OK);
    }

    @GetMapping("/room")
    public List<RoomDto> getAllRooms() throws IOException {

        List<Room> rooms = this.roomService.getAllRooms();
        this.roomService.addRoomsToDb(rooms);
        return this.roomService.convertToDto(rooms);

    }

}
