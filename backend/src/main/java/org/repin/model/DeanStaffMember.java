package org.repin.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
@Table(name="dean_staff")
public class DeanStaffMember {
    @Id
    UUID id;
    String name;
    String email;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="faculty_id")
    Faculty faculty;
    String password;

}
