package org.repin.repository;

import org.repin.model.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, UUID> {

//    @Query("SELECT a FROM Attendance  WHERE a.studentId = :studentId AND a.scheduleId = :scheduleId AND a.timestamp = :timestamp")
//    Optional<Attendance> checkExistance(@Param("studentId") UUID studentId, @Param("scheduleId") UUID scheduleId, @Param("timestamp") LocalDateTime timestamp);

    @Query("SELECT a FROM Attendance a WHERE a.scheduleItem.id = :scheduleId")
    List<Attendance> findByScheduleId(@Param("scheduleId") UUID scheduleId);

    @Query("SELECT a FROM Attendance a WHERE a.scheduleItem.id = :scheduleId AND a.student.id = :studentId AND a.timestamp = :timestamp")
    Optional<Attendance> findExisting(@Param("scheduleId") UUID scheduleId, @Param("studentId") UUID studentId, @Param("timestamp") LocalDateTime timestamp);

    @Query("SELECT a FROM Attendance a WHERE " +
            "a.scheduleItem.id = :scheduleId AND " +
            "a.student.id IN :studentIds AND " +
            "a.timestamp = :timestamp")
    List<Attendance> findByScheduleIdAndStudentIdInAndTimestamp(
            @Param("scheduleId") UUID scheduleId,
            @Param("studentIds") List<UUID> studentIds,
            @Param("timestamp") LocalDateTime timestamp);
}
