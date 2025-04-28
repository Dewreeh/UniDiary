package org.repin.dto.request_dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.repin.enums.LessonType;
import org.repin.enums.WeekType;
import org.repin.enums.Weekday;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Data
public class ScheduleAddDto {
    @NotNull
    @Schema(description = "Список групп, которые привязываются к занятию")
    List<UUID> groupsIds;
    @NotNull
    UUID lecturerId;
    @NotNull
    UUID disciplineId;
    @NotNull
    Weekday weekday;
    @NotNull
    WeekType weekType;
    @NotNull
    LessonType lessonType;
    @NotNull
    LocalTime startTime;
}
