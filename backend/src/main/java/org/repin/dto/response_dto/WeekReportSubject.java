package org.repin.dto.response_dto;

import lombok.Data;
import org.repin.model.ScheduleItem;
import org.repin.model.Student;
import org.repin.model.StudentAttendance;

import javax.security.auth.Subject;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
public class WeekReportSubject {
    UUID groupId;
    List<LocalDate> dates;
    List<Student> students;

}
