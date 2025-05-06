package org.repin.repository;

import org.repin.model.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, UUID> {

    @Query("SELECT a FROMa Attendance  WHERE a.studentId = :studentId AND a.scheduleId = :scheduleId AND a.timestamp = :timestamp")
    Optional<Attendance> checkExistance(@Param("studentId") UUID studentId, @Param("scheduleId") UUID scheduleId, @Param("timestamp") LocalDateTime timestamp);
}
