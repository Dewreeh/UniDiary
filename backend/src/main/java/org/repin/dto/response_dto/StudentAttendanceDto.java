package org.repin.dto.response_dto;

import java.util.UUID;

public record StudentAttendanceDto(UUID studentId, String studentName, Boolean attendanceStatus) {}