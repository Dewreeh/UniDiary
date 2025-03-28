package org.repin.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
@Table(name="dean_staff")
public class DeanStaffMember implements AppUser{
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
    public DeanStaffMember(){}
    public DeanStaffMember(String name, String email, Faculty faculty, String password) {
        this.name = name;
        this.email = email;
        this.faculty = faculty;
        this.password = password;
    }

    @Override
    public String getRole() {
        return "ROLE_DEAN_STAFF";
    }

}
