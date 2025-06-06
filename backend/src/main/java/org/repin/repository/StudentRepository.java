package org.repin.repository;

import org.repin.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StudentRepository extends JpaRepository<Student, UUID> {
    Optional<Student> findByEmail(String email);

    @Query("SELECT s FROM Student s WHERE s.studentGroup.faculty.id = :facultyId")
    List<Student> findByFacultyId(@Param("facultyId") UUID facultyId);

    @Query("SELECT s FROM Student s WHERE s.studentGroup.faculty.id = :facultyId AND s.isHeadman = true")
    List<Student> findHeadmanByFacultyId(@Param("facultyId") UUID facultyId);
    List<Student> findByStudentGroupId(UUID studentGroup);

    List<Student> findByIsHeadmanTrue();

    @Query("SELECT s.studentGroup.id FROM Student s WHERE s.id = :id")
    UUID getGroupIdByStudentId(@Param("id") UUID id);

}
