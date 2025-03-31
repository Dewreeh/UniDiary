package org.repin.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "specialities")
public class Speciality {
    @Id
    @GeneratedValue
    UUID id;

    String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="faculty_id")
    Faculty faculty;

    Speciality(){}
}


