package org.repin.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
@Table(name="lecturers")
public class Lecturer implements AppUser{
    @Id
    @GeneratedValue
    UUID id;
    String name;
    String email;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="faculty_id")
    Faculty faculty;
    @JsonIgnore
    String password;

    @Override
    public String getRole() {
        return "ROLE_LECTURER";
    }
}
