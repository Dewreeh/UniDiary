package org.repin.dto.request_dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.repin.enums.LessonType;
import org.repin.enums.WeekType;
import org.repin.enums.Weekday;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Data
public class ScheduleAddDto {
    @NotBlank @Schema(description = "Список групп, которые привязываются к занятию")
    List<UUID> groupsIds;
    @NotBlank
    UUID lecturerId;
    @NotBlank
    UUID disciplineId;
    @NotBlank
    Weekday weekday;
    @NotBlank
    WeekType weekType;
    @NotBlank
    LessonType lessonType;
    @NotBlank
    LocalTime startTime;
}
