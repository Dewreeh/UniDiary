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
    @Query("""
    SELECT DISTINCT si FROM ScheduleItem si
    JOIN FETCH si.lecturer
    JOIN FETCH si.discipline
    JOIN FETCH si.groups g
    JOIN FETCH g.faculty f
    WHERE (:groupId IS NULL OR g.id = :groupId)
    AND (:facultyId IS NULL OR f.id = :facultyId)
    AND (:weekday IS NULL OR si.weekday = :weekday)
    AND (:lecturerId IS NULL OR si.lecturer.id = :lecturerId)
    AND (:disciplineId IS NULL OR si.discipline.id = :disciplineId)
    ORDER BY si.weekday, si.startTime
""")
    List<ScheduleItem> findByFilters(
            @Param("groupId") UUID groupId,
            @Param("facultyId") UUID facultyId,
            @Param("weekday") Weekday weekday,
            @Param("lecturerId") UUID lecturerId,
            @Param("disciplineId") UUID disciplineId
    );
}