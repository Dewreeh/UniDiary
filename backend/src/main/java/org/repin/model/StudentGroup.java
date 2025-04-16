package org.repin.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Data
@Table(name="student_groups")
public class StudentGroup {
    @Id
    @GeneratedValue
    UUID id;

    String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="speciality_id")
    Speciality speciality;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="faculty_id")
    Faculty faculty;

    String email;

    @ManyToMany(mappedBy = "groups")
    @JsonIgnore
    private Set<ScheduleItem> scheduleItems = new HashSet<>();

    public StudentGroup(String name, Speciality speciality, Faculty faculty, String email) {
        this.name = name;
        this.speciality = speciality;
        this.faculty = faculty;
        this.email = email;
    }

    StudentGroup(){}
}
