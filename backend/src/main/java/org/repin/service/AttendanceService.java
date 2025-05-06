package org.repin.service;

import org.repin.dto.request_dto.AttendanceDto;
import org.repin.dto.request_dto.AttendanceRecordDto;
import org.repin.dto.response_dto.AttendanceFormResponse;
import org.repin.dto.response_dto.StudentAttendanceDto;
import org.repin.model.Attendance;
import org.repin.model.ScheduleItem;
import org.repin.model.Student;
import org.repin.repository.AttendanceRepository;
import org.repin.repository.ScheduleItemRepository;
import org.repin.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final StudentRepository studentRepository;
    private final ScheduleItemRepository scheduleItemRepository;
    private final StudentsService studentsService;


    @Autowired
    AttendanceService(AttendanceRepository attendanceRepository,
                      StudentRepository studentRepository,
                      ScheduleItemRepository scheduleItemRepository,
                      StudentsService studentsService){
        this.attendanceRepository = attendanceRepository;
        this.studentRepository = studentRepository;
        this.scheduleItemRepository = scheduleItemRepository;
        this.studentsService = studentsService;
    }


    @Transactional
    public List<Attendance> markAttendance(AttendanceRecordDto dto){

//       List<AttendanceDto> attendanceList = dto.getAttendanceList();
//
//       List<UUID> studentIds = attendanceList.stream().map(AttendanceDto::getStudentId).toList();
//
//       Map<UUID, Student> studentEntities = studentRepository.findAllById(studentIds)
//               .stream()
//               .collect(Collectors.toMap(Student::getId, student -> student));
//
//       ScheduleItem scheduleItemEntity = scheduleItemRepository.findById(attendanceList.getFirst().getScheduleItemId()).orElseThrow();
//
//       List<Attendance> attendances = attendanceList.stream().map(elem -> mapToEntity(elem, scheduleItemEntity, studentEntities)).toList();
//
//       attendanceRepository.saveAll(attendances);

       if(!Objects.equals(dto.getTimestamp().getDayOfYear(), LocalDateTime.now().getDayOfYear())){
           throw new IllegalStateException("Поставить отметку за конкретный день нельзя заранее");
       }

       List<Attendance> attendances =  dto.getAttendanceList().stream().map(
               elem -> {
                   Attendance attendance = attendanceRepository.findExisting(dto.getScheduleItemId(), elem.getStudentId(), dto.getTimestamp())
                           .orElse(new Attendance());

                   attendance.setStudent(studentRepository.getReferenceById(elem.getStudentId()));
                   attendance.setScheduleItem(scheduleItemRepository.getReferenceById(dto.getScheduleItemId()));
                   attendance.setAttendanceStatus(elem.getAttendanceStatus());
                   attendance.setTimestamp(dto.getTimestamp());

                   return attendance;
               }
       ).toList();

       attendanceRepository.saveAll(attendances);

       return attendances;
    }

    public AttendanceFormResponse getAttendanceInfo(UUID groupId, UUID scheduleId){
        List<Student> students = studentRepository.findByStudentGroupId(groupId);

        ScheduleItem scheduleItem = scheduleItemRepository.findById(scheduleId).orElseThrow();

        LocalDateTime timestamp = LocalDateTime.of(LocalDate.now(), scheduleItem.getStartTime());

        List<UUID> studentIds = students.stream()
                .map(Student::getId)
                .collect(Collectors.toList());

        Map<UUID, Boolean> existingAttendance = attendanceRepository
                .findByScheduleIdAndStudentIdInAndTimestamp(scheduleId, studentIds, timestamp)
                .stream()
                .collect(Collectors.toMap(
                        a -> a.getStudent().getId(),
                        a -> a.getAttendanceStatus()
                ));

        List<StudentAttendanceDto> studentAttendances = students.stream()
                .map(student -> new StudentAttendanceDto(
                        student.getId(),
                        student.getName(),
                        existingAttendance.getOrDefault(student.getId(), null)
                ))
                .collect(Collectors.toList());

        return new AttendanceFormResponse(scheduleId, groupId, studentAttendances);
    }

}
