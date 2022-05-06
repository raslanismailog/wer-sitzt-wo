package de.octagen.wersitztwo.repository;

import de.octagen.wersitztwo.model.PersonDto;
import de.octagen.wersitztwo.model.Room;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {
    List<Room> findAll(Specification<PersonDto> specification);
}