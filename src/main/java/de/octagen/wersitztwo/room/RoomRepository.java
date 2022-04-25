package de.octagen.wersitztwo.room;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Integer> {
    @Query(value = "select * from room left join person p on room.room_id = p.room_id", nativeQuery = true)
    List<Room> findRooms();

}