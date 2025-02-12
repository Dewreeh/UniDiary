package org.repin.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
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

    public Faculty(){};
    public Faculty(String name, String address, String phoneNumber) {
        this.name = name;
        this.address = address;
        this.phone_number = phoneNumber;
    }
}
