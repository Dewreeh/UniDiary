package org.repin.repository;

import org.repin.model.Semester;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SemesterRepository extends JpaRepository<Semester, UUID> {

    Semester findByIsCurrentTrue();
}
