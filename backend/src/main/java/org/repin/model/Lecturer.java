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
    UUID id;
    String name;

    StudentGroup studentGroups;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="groups_id")
    Faculty faculty;
    @JsonIgnore
    String password;
}
