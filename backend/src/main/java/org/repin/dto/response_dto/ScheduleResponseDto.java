package org.repin.dto.response_dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.repin.dto.request_dto.StudentGroupDto;
import org.repin.enums.LessonType;
import org.repin.enums.WeekType;
import org.repin.enums.Weekday;
import org.repin.model.ScheduleItem;
import org.repin.model.StudentGroup;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleResponseDto {
    private UUID id;

    private List<StudentGroup> groups;

    private UUID lecturerId;
    private String lecturerName;

    private UUID disciplineId;
    private String disciplineName;

    private Weekday weekday;
    private WeekType weekType;
    private LessonType lessonType;
    private LocalTime startTime;
    private LocalTime endTime;
}