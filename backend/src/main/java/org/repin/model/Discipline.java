package org.repin.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.UUID;

@Entity
public class Discipline {
    @Id
    @GeneratedValue
    private UUID id;

    private String name;

}
