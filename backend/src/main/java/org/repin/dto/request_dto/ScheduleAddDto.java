package org.repin.dto.request_dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.repin.enums.WeekType;
import org.repin.enums.Weekday;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class ScheduleAddDto {
    @NotBlank @Schema(description = "Список групп, которые привязываются к занятию")
    List<UUID> groupsIds;
    @NotBlank
    UUID lecturerId;
    @NotBlank
    UUID discipline_id;
    @NotBlank
    Weekday weekday;
    @NotBlank
    WeekType weekType;
    @NotBlank
    LocalDateTime startTime;
    @NotBlank
    LocalDateTime endTime;
}
