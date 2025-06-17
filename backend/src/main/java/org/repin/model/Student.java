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

    Boolean isHeadman;

    public Student(String name, StudentGroup studentGroup, String password, String email, Boolean isHeadman) {
        this.name = name;
        this.studentGroup = studentGroup;
        this.password = password;
        this.email = email;
        this.isHeadman = isHeadman;
    }

    public Student(){}
    @Override
    public String getRole() {
        if(this.isHeadman) return "HEADMAN";
        return "STUDENT";
    }

}

