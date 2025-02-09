package org.repin.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
@Table(name="faculties")
public class Faculty {
    @Id
    private UUID id;
    private String name;
    private String address;
    private String phone_number;
}
