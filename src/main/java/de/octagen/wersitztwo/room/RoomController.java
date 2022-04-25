package de.octagen.wersitztwo.room;

import de.octagen.wersitztwo.person.PersonRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("api")
public class RoomController {

    private final PersonRepository personRepository;
    private final RoomRepository roomRepository;
    private final RoomService roomService;
    public RoomController(RoomService roomService, RoomRepository roomRepository, PersonRepository personRepository) {
        this.roomService = roomService;
        this.roomRepository = roomRepository;
        this.personRepository = personRepository;
    }

    @PostMapping(value = "import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<RoomDto>> getFile(@RequestParam("file") MultipartFile file) throws IOException {
        Date date = new Date();
        byte[] data = file.getBytes();
        String p = "./%d_%s".formatted(date.getTime(), file.getOriginalFilename());
        Path path = Paths.get(p);
        Files.write(path, data);
        List<Room> rooms = this.roomService.addRoomsToDb(this.roomService.getAllRooms(p));
        List<RoomDto> roomsDto = this.roomService.convertToDto(rooms);

        return new ResponseEntity<>(roomsDto, HttpStatus.CREATED);
    }

    @GetMapping("/room/{id}")
    public RoomDto getOneRoom(@PathVariable String id) {
        return this.roomService.getDto(this.roomRepository
                .findById(Integer.parseInt(id))
                .orElseThrow(() -> new NoSuchElementException("Room not found")));
    }


    @GetMapping("/room")
    public List<RoomDto> getAllRooms(){
        List<Room> rooms = this.roomRepository.findRooms();
        return this.roomService.convertToDto(rooms);

    }

}
