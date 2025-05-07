package org.repin.controller;

import jakarta.validation.Valid;
import org.repin.dto.request_dto.StudentDto;
import org.repin.dto.response_dto.GeneratedPasswordDto;
import org.repin.dto.response_dto.GenericTableDataDto;
import org.repin.model.Student;
import org.repin.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api")
public class StudentsController {

    private final StudentsService studentsService;


    @Autowired
    StudentsController(StudentsService studentsService) {
        this.studentsService = studentsService;
    }

    @GetMapping("/get_students")
    ResponseEntity<GenericTableDataDto<Student>> getStudents(@RequestParam(name = "userId") UUID deanStaffId) {

        return ResponseEntity.ok().body(studentsService.getStudents(deanStaffId));
    }

    @GetMapping("/get_students_by_group")
    ResponseEntity<GenericTableDataDto<Student>> getStudentsByGroup(@RequestParam(name = "groupId") UUID groupId) {

        return ResponseEntity.ok().body(studentsService.getStudentsByGroup(groupId));
    }

    @PostMapping("/add_student")
    ResponseEntity<Student> addStudentAndGeneratePassword(@Valid @RequestBody StudentDto dto) {

        return ResponseEntity.ok().body(studentsService.addStudent(dto));
    }

    @DeleteMapping("/delete_student")
    ResponseEntity<Student> deleteStudent(@RequestParam("studentId") UUID studentId) {

        studentsService.deleteStudent(studentId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/get_headmen")
    ResponseEntity<GenericTableDataDto<Student>> getHeadmen(@RequestParam("userId") UUID deanStaffId){

        return ResponseEntity.ok().body(studentsService.getHeadmen(deanStaffId));
    }

    @PostMapping("/add_headman")
    ResponseEntity<GeneratedPasswordDto> promoteStudentToHeadman(@RequestParam("studentId") UUID studentId){

        return ResponseEntity.ok().body(studentsService.promoteStudentToHeadman(studentId));
    }

}