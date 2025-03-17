package org.repin.repository;

import org.repin.model.StudentGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StudentGroupsRepository extends JpaRepository<StudentGroup, UUID> {
}
