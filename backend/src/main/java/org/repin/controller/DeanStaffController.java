package org.repin.controller;

import io.micrometer.common.lang.Nullable;
import jakarta.validation.Valid;
import org.repin.dto.request_dto.*;
import org.repin.dto.response_dto.GeneratedPasswordDto;
import org.repin.dto.response_dto.GenericTableDataDto;
import org.repin.dto.response_dto.ScheduleResponseDto;
import org.repin.enums.Weekday;
import org.repin.model.*;
import org.repin.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class DeanStaffController {

    private final GroupsService groupsService;
    private final StudentsService studentsService;
    private final DisciplinesService disciplinesService;
    private final LecturerService lecturerService;
    private final SchedulesService schedulesService;

    @Autowired
    DeanStaffController(GroupsService groupsService,
                        StudentsService studentsService,
                        DisciplinesService disciplinesService,
                        LecturerService lecturerService,
                        SchedulesService schedulesService){
        this.groupsService = groupsService;
        this.studentsService = studentsService;
        this.disciplinesService = disciplinesService;
        this.lecturerService = lecturerService;
        this.schedulesService = schedulesService;
    }

    @GetMapping("/get_students")
    ResponseEntity<GenericTableDataDto<Student>> getStudents(@RequestParam("userId") UUID deanStaffId){

        return ResponseEntity.ok().body(studentsService.getStudents(deanStaffId));
    }

    @PostMapping("/add_student")
    ResponseEntity<Student> addStudentAndGeneratePassword(@Valid @RequestBody StudentDto dto){

        return ResponseEntity.ok().body(studentsService.addStudent(dto));
    }


    @GetMapping("/get_groups")
    public ResponseEntity<GenericTableDataDto<StudentGroup>> getStudentGroups(@RequestParam("userId") UUID deanStaffId) {

        return ResponseEntity.ok(groupsService.getStudentGroups(deanStaffId));
    }

    @PostMapping("/add_group")
    public ResponseEntity<Object> addStudentGroup(@Valid @RequestBody StudentGroupDto dto){

        return ResponseEntity.ok().body(groupsService.addStudentGroup(dto));
    }

    @GetMapping("/get_disciplines")
    public ResponseEntity<GenericTableDataDto<Discipline>> getDisciplines(@Nullable @RequestParam("userId") UUID deanStaffId) {

        return ResponseEntity.ok(disciplinesService.getDisciplines(deanStaffId));
    }

    @PostMapping("/add_discipline")
    public ResponseEntity<Object> addDiscipline(@Valid @RequestBody DisciplineDto dto){

        return ResponseEntity.ok().body(disciplinesService.addDiscipline(dto)); //TODO тут не dto Надо возвращать, а сущность. но там пока с этим проблема из-за прокси Faculty
    }

    @GetMapping("/get_headmen")
    ResponseEntity<GenericTableDataDto<Student>> getHeadmen(@RequestParam("userId") UUID deanStaffId){

        return ResponseEntity.ok().body(studentsService.getHeadmen(deanStaffId));
    }

    @PostMapping("/add_headman")
    ResponseEntity<GeneratedPasswordDto> promoteStudentToHeadman(@RequestParam("studentId") UUID studentId){

        return ResponseEntity.ok().body(studentsService.promoteStudentToHeadman(studentId)); //сохраняем сущность и возвращаем пароль
    }

    @GetMapping("/get_lecturers")
    ResponseEntity<GenericTableDataDto<Lecturer>> getLecturers(){

        return ResponseEntity.ok().body(lecturerService.getLecturers());
    }

    @PostMapping("/add_lecturer")
    ResponseEntity<GeneratedPasswordDto> addLecturer(@Valid @RequestBody LecturerDto lecturerDto){

        return ResponseEntity.ok().body(lecturerService.addLecturer(lecturerDto)); //сохраняем сущность и возвращаем пароль
    }

    @GetMapping("/get_schedules_for_faculty")
    ResponseEntity<List<ScheduleResponseDto>> getSchedules(@RequestParam("userId") UUID userId,
                                                           @RequestParam(name="groupId", required = false) UUID groupId,
                                                           @RequestParam(name="weekday", required = false) Weekday weekday,
                                                           @RequestParam(name="lecturerId", required = false) UUID lecturerId,
                                                           @RequestParam(name="disciplineId", required = false) UUID disciplineId){

        return ResponseEntity.ok().body(schedulesService.getSchedulesForFaculty(
                        userId,
                        groupId,
                        weekday,
                        lecturerId,
                        disciplineId)
        );
    }

    @PostMapping("/add_schedule_item")
    ResponseEntity<ScheduleItem> addScheduleItem(@Valid @RequestBody ScheduleAddDto dto){

        return ResponseEntity.ok().body(schedulesService.createScheduleItem(dto)); //сохраняем сущность и возвращаем пароль
    }
}
