package org.repin.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.util.UUID;
import lombok.Data;

@Entity
@Data
@Table(name="faculties")
public class Faculty{
    @Id
    @GeneratedValue
    private UUID id;

    private String name;

    private String email;

    @JsonProperty("phoneNumber")
    private String phone_number;

    public Faculty(){};
    public Faculty(String name, String email, String phoneNumber) {
        this.name = name;
        this.email = email;
        this.phone_number = phoneNumber;
    }
}
