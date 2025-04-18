package org.repin.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "disciplines")
public class Discipline {
    @Id
    @GeneratedValue
    private UUID id;

    private String name;

}
