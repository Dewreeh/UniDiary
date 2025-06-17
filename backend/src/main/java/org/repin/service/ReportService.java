package org.repin.service;

import org.repin.dto.response_dto.ConcreteSchedule;
import org.repin.dto.response_dto.WeekReportGeneral;
import org.repin.dto.response_dto.WeekReportGeneralStudent;
import org.repin.dto.response_dto.WeekReportGeneralStudent;
import org.repin.model.Attendance;
import org.repin.model.ScheduleItem;
import org.repin.model.Student;
import org.repin.repository.AttendanceRepository;
import org.repin.repository.ScheduleItemRepository;
import org.repin.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class ReportService {

    AttendanceService attendanceService;
    ScheduleItemRepository scheduleItemRepository;
    SchedulesService schedulesService;
    StudentRepository studentRepository;
    AttendanceRepository attendanceRepository;

    @Autowired
    ReportService(AttendanceService attendanceService,
                  ScheduleItemRepository scheduleItemRepository,
                  SchedulesService schedulesService,
                  StudentRepository studentRepository,
                  AttendanceRepository attendanceRepository){
        this.attendanceService = attendanceService;
        this.scheduleItemRepository = scheduleItemRepository;
        this.schedulesService = schedulesService;
        this.studentRepository = studentRepository;
        this.attendanceRepository = attendanceRepository;
    }

    public WeekReportGeneral getWeekReport(LocalDate startDate, LocalDate endDate, UUID groupId){
        List<Student> students = studentRepository.findByStudentGroupId(groupId);

        List<ScheduleItem> scheduleItems = scheduleItemRepository.findByFilters(groupId, null, null, null, null);

        List<ConcreteSchedule> schedules = schedulesService.buildConcreteSchedule(scheduleItems, startDate, endDate);

        List<LocalDate> dates = schedules.stream()
                .map(ConcreteSchedule::getDate)
                .toList();

        WeekReportGeneral report = new WeekReportGeneral();

        report.setStartDate(startDate);
        report.setEndDate(endDate);
        report.setDates(dates);


        List<WeekReportGeneralStudent> studentReports = students.stream().map(
                student -> {
                    WeekReportGeneralStudent studentReport = new WeekReportGeneralStudent();
                    studentReport.setStudentId(student.getId());
                    studentReport.setStudentName(student.getName());

                    Map<LocalDate, Map<String, Boolean>> attendanceByDate = new LinkedHashMap<>();

                    int totalLessons = 0;
                    int attendedLessons = 0;

                    for(ConcreteSchedule daySchedule: schedules){
                        LocalDate date = daySchedule.getDate();

                        daySchedule.getSchedulesInfo()
                                .forEach(lesson -> {

                                    LocalDateTime timestamp = LocalDateTime.of(date, lesson.getStartTime());

                                    Optional<Attendance> statusOpt = attendanceRepository
                                            .findByScheduleIdAndStudentIdAndTimestamp(lesson.getId(), student.getId(), timestamp);
                                    Boolean status = false;
                                    if(statusOpt.isPresent()){
                                        status = statusOpt.get().getAttendanceStatus();
                                    }


                                    String subjectKey = lesson.getDisciplineName() + " (" +
                                            lesson.getStartTime() + ")";


                                    attendanceByDate
                                            .computeIfAbsent(date, k -> new HashMap<>()).put(subjectKey, status);

                                });
                        Map<String, Boolean> dayAttendance = attendanceByDate.get(date);
                        if (dayAttendance != null) {
                            totalLessons += dayAttendance.size();
                            attendedLessons += (int) dayAttendance.values().stream().filter(Boolean::booleanValue).count();
                        }

                    }
                    studentReport.setAttendanceRate(getAttendanceRate(totalLessons, attendedLessons));
                    studentReport.setAttendanceByDate(attendanceByDate);
                    return studentReport;
                }
        ).toList();

        report.setStudents(studentReports);
        return report;
    }

    private Double getAttendanceRate(int countOfLessons, int countOfAttendance){
        return countOfLessons != 0 ? (double) Math.round(((double) countOfAttendance / countOfLessons) * 10000) / 100 : 0;
    }
}
