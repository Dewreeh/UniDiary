package org.repin.repository;

import org.repin.model.DeanStaffMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DeanStaffRepository extends JpaRepository<DeanStaffMember, UUID> {
    Optional<DeanStaffMember> findByEmail(String email);
}
