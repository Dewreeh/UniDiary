package org.repin.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
@Table(name="lecturers")
public class Lecturer {
    @Id
    @GeneratedValue
    UUID id;
    String name;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="faculty_id")
    Faculty faculty;
    @JsonIgnore
    String password;
}
