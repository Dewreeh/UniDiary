package org.repin.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
@Table(name="students")
public class Student implements AppUser{
    @Id
    @GeneratedValue
    UUID id;

    String name;

    String email;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="group_id")
    StudentGroup studentGroup;

    @JsonIgnore
    String password;

    public Student(String name, StudentGroup studentGroup, String password, String email) {
        this.name = name;
        this.studentGroup = studentGroup;
        this.password = password;
        this.email = email;
    }

    public Student(){}
    @Override
    public String getRole() {
        return "ROLE_STUDENT";
    }

}

