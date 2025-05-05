package org.repin.service;

import org.repin.dto.request_dto.AttendanceDto;
import org.repin.dto.request_dto.AttendanceRecordDto;
import org.repin.model.Attendance;
import org.repin.model.ScheduleItem;
import org.repin.model.Student;
import org.repin.repository.AttendanceRepository;
import org.repin.repository.ScheduleItemRepository;
import org.repin.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final StudentRepository studentRepository;
    private final ScheduleItemRepository scheduleItemRepository;


    @Autowired
    AttendanceService(AttendanceRepository attendanceRepository,
                      StudentRepository studentRepository,
                      ScheduleItemRepository scheduleItemRepository){
        this.attendanceRepository = attendanceRepository;
        this.studentRepository = studentRepository;
        this.scheduleItemRepository = scheduleItemRepository;
    }


    @Transactional
    public List<Attendance> markAttendanceForScheduleItem(AttendanceRecordDto dto){
       List<AttendanceDto> attendanceList = dto.getAttendanceList();

       List<UUID> studentIds = attendanceList.stream().map(AttendanceDto::getStudentId).toList();

       Map<UUID, Student> studentEntities = studentRepository.findAllById(studentIds)
               .stream()
               .collect(Collectors.toMap(Student::getId, student -> student));

        ScheduleItem scheduleItemEntity = scheduleItemRepository.findById(attendanceList.getFirst().getScheduleItemId()).orElseThrow();

       List<Attendance> attendances = attendanceList.stream().map(elem -> mapToEntity(elem, scheduleItemEntity, studentEntities)).toList();

       attendanceRepository.saveAll(attendances);

       return attendances;
    }

    private Attendance mapToEntity(AttendanceDto dto, ScheduleItem schedule, Map<UUID, Student> students){
        return Attendance.builder()
                .student(students.get(dto.getStudentId()))
                .scheduleItem(schedule)
                .attendanceStatus(dto.getAttendanceStatus())
                .timestamp(dto.getTimestamp())
                .build();
    }
}
