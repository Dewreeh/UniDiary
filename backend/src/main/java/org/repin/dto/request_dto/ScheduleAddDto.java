package org.repin.dto.request_dto;

import lombok.Data;
import org.repin.enums.WeekType;
import org.repin.enums.Weekday;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class ScheduleAddDto {
    List<UUID> groupsIds;
    UUID lecturerId;
    UUID discipline_id;
    Weekday weekday;
    WeekType weekType;
    LocalDateTime startTime;
    LocalDateTime endTime;
}
