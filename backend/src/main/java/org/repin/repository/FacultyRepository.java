package org.repin.repository;


import org.repin.model.Faculty;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FacultyRepository extends CrudRepository<Faculty, UUID> {
    Iterable<Faculty> findAll();
}
