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
    @GeneratedValue
    UUID id;
    String name;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="group_id")
    StudentGroup studentGroup;
    @JsonIgnore
    String password;

    public Student(String name, StudentGroup studentGroup, String password) {
        this.name = name;
        this.studentGroup = studentGroup;
        this.password = password;
    }
}

