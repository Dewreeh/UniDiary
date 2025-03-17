package org.repin.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
@Table(name="students")
public class Student {
    @Id
    UUID id;
    String name;

    StudentGroup studentGroup;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="groups_id")
    Faculty faculty;
    @JsonIgnore
    String password;

    public Student(String name, StudentGroup studentGroup, Faculty faculty, String password) {
        this.name = name;
        this.studentGroup = studentGroup;
        this.faculty = faculty;
        this.password = password;
    }
}

