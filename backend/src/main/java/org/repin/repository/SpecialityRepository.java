package org.repin.repository;

import org.repin.model.Speciality;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpecialityRepository extends JpaRepository<Speciality, UUID> {
}
