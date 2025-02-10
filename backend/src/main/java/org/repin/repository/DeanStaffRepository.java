package org.repin.repository;

import org.repin.model.DeanStaffMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DeanStaffRepository extends JpaRepository<DeanStaffMember, UUID> {
}
