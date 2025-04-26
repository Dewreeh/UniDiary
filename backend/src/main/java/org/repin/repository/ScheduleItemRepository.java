package org.repin.repository;

import org.repin.dto.request_dto.ScheduleAddDto;
import org.repin.enums.Weekday;
import org.repin.model.ScheduleItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ScheduleItemRepository extends JpaRepository<ScheduleItem, UUID> {


    @Query("SELECT s FROM ScheduleItem s WHERE s.lecturer = :lecturerId AND s.discipline = :disciplineId AND s.weekday = :weekday AND s.startTime = :startTime AND s.endTime = :endTime AND s.semester = :semesterId")
    Optional<ScheduleItem> checkExistance(UUID lecturerId, UUID disciplineId, Weekday weekday, LocalTime startTime);

}
