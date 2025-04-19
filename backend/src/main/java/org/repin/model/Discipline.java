package org.repin.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Table(name = "disciplines")
@Data
public class Discipline {
    @Id
    @GeneratedValue
    private UUID id;

    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "faculty_id")
    private Faculty faculty;


    public Discipline(String name, Faculty faculty) {
        this.name = name;
        this.faculty = faculty;
    }

    Discipline(){}
}
