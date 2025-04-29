package org.repin.repository;

import org.repin.enums.Weekday;
import org.repin.model.ScheduleItem;
import org.repin.model.Semester;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ScheduleItemRepository extends JpaRepository<ScheduleItem, UUID> {


    @Query("SELECT s FROM ScheduleItem s WHERE s.lecturer.id = :lecturerId AND s.discipline.id = :disciplineId AND s.weekday = :weekday AND s.startTime = :startTime AND s.semester.id = :semesterId")
    Optional<ScheduleItem> checkExistance(@Param("lecturerId") UUID lecturerId, @Param("disciplineId") UUID disciplineId, @Param("weekday") Weekday weekday, @Param("startTime") LocalTime startTime, @Param("semesterId") UUID semesterId);

    @EntityGraph(attributePaths = {"lecturer", "discipline", "groups.faculty"})
    @Query("SELECT DISTINCT si FROM ScheduleItem si JOIN si.groups g WHERE g.faculty.id = :facultyId ORDER BY si.weekday, si.startTime ASC")
    List<ScheduleItem> findByFacultyIdWithGroups(@Param("facultyId") UUID facultyId);
}