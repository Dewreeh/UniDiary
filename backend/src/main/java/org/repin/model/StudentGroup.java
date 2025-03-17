package org.repin.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
@Table(name="student_groups")
public class StudentGroup {
    @Id
    UUID id;
    String name;
    String speciality; //TODO сделать это через ENUM
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="faculty_id")
    Faculty faculty;
    String email;

    public StudentGroup(String name, String speciality, Faculty faculty, String email) {
        this.name = name;
        this.speciality = speciality;
        this.faculty = faculty;
        this.email = email;
    }
}
