package org.repin.dto.request_dto;

import lombok.Data;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;

@Data
public class SemesterDto {
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean isCurrent;
}
