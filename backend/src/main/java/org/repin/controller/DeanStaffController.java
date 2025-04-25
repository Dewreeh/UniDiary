package org.repin.controller;

import jakarta.validation.Valid;
import org.repin.dto.request_dto.DisciplineDto;
import org.repin.dto.request_dto.LecturerDto;
import org.repin.dto.request_dto.StudentDto;
import org.repin.dto.request_dto.StudentGroupDto;
import org.repin.dto.response_dto.GeneratedPasswordDto;
import org.repin.dto.response_dto.GenericTableDataDto;
import org.repin.model.Discipline;
import org.repin.model.Lecturer;
import org.repin.model.Student;
import org.repin.model.StudentGroup;
import org.repin.service.DisciplinesService;
import org.repin.service.GroupsService;
import org.repin.service.LecturerService;
import org.repin.service.StudentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api")
public class DeanStaffController {

    private final GroupsService groupsService;
    private final StudentsService studentsService;
    private final DisciplinesService disciplinesService;
    private final LecturerService lecturerService;

    @Autowired
    DeanStaffController(GroupsService groupsService,
                        StudentsService studentsService,
                        DisciplinesService disciplinesService,
                        LecturerService lecturerService){
        this.groupsService = groupsService;
        this.studentsService = studentsService;
        this.disciplinesService = disciplinesService;
        this.lecturerService = lecturerService;
    }

    @GetMapping("/get_students")
    ResponseEntity<GenericTableDataDto<Student>> getStudents(@RequestParam("userId") UUID deanStaffId){

        return ResponseEntity.ok().body(studentsService.getStudents(deanStaffId));
    }

    @PostMapping("add_student")
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
    public ResponseEntity<GenericTableDataDto<Discipline>> getDisciplines(@RequestParam("userId") UUID deanStaffId) {

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
}
