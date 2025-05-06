package org.repin.dto.response_dto;

import java.util.List;
import java.util.UUID;

public record AttendanceFormResponse(UUID scheduleId, UUID groupId, List<StudentAttendanceDto> students
) {}