package org.repin.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
@Table(name="faculties")
public class Faculty {
    @Id
    @GeneratedValue
    private UUID id;
    private String name;
    private String address;
    private String phone_number;

    public Faculty(String name, String address, String phoneNumber) {
    }
}
