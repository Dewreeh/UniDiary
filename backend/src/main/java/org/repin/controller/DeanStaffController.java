package org.repin.controller;

import jakarta.validation.Valid;
import org.repin.dto.*;
import org.repin.model.Student;
import org.repin.model.StudentGroup;
import org.repin.repository.StudentGroupsRepository;
import org.repin.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/api")
public class DeanStaffController {

    private final StudentGroupsRepository studentGroupsRepository;
    private final StudentRepository studentRepository;

    @Autowired
    DeanStaffController(StudentGroupsRepository studentGroupsRepository,
                        StudentRepository studentRepository){
        this.studentGroupsRepository = studentGroupsRepository;
        this.studentRepository = studentRepository;
    }

    @PostMapping("/get_students")
    ResponseEntity<Object> getStudents(){
        List<Student> students = studentRepository.findAll();
        List<String> headers = List.of("#", "ФИО", "Группа", "Почта");
        return ResponseEntity.ok().body(new GenericTableDataDto<Student>(headers, students));
    }

    @PostMapping("add_staff_member")
    ResponseEntity<Object> addStudentAndGenerateDto(@Valid @RequestBody StudentDto dto){
            Student student = new Student(dto.getName(),
                    dto.getStudentGroup(),
                    dto.getFaculty(),
                    UUID.randomUUID()
                            .toString()
                            .substring(0, 8));
        return ResponseEntity.ok().body(new GeneratedPasswordDto(studentRepository.save(student).getPassword())); //сохраняем сущность и возвращаем пароль
    }


    @PostMapping("/get_groups")
    public ResponseEntity<Object> getStudentGroups(){
        List<StudentGroup> studentGroups = studentGroupsRepository.findAll();
        List<String> headers = List.of("#", "Название", "Факультет", "Почта группы");
        return ResponseEntity.ok().body(new GenericTableDataDto<StudentGroup>(headers, studentGroups));
    }

    // Добавление новой группы
    @PostMapping("/add_group")
    public ResponseEntity<Object> addStudentGroup(@Valid @RequestBody StudentGroupDto dto){
        StudentGroup studentGroup = new StudentGroup(dto.getName(),
                dto.getSpeciality(),
                dto.getFaculty(),
                dto.getEmail());
        return ResponseEntity.ok().body(studentGroupsRepository.save(studentGroup));
    }
}
