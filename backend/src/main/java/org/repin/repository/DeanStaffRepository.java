package org.repin.repository;

import org.repin.model.DeanStaffMember;
import org.repin.model.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DeanStaffRepository extends JpaRepository<DeanStaffMember, UUID> {
    Optional<DeanStaffMember> findByEmail(String email);
    @Query("SELECT d.faculty.id FROM DeanStaffMember d WHERE d.id = :staffId")
    Optional<UUID> findFacultyByStaffId(@Param("staffId") UUID staffId);
}
