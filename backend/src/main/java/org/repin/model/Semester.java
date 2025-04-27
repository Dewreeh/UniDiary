package org.repin.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "semesters")
@Data
public class Semester {
    @Id
    @GeneratedValue
    private UUID id;

    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean isCurrent;

    public Semester(){}
}
