package org.repin.controller;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.repin.dto.request_dto.StudentDto;
import org.repin.dto.request_dto.StudentGroupDto;
import org.repin.dto.response_dto.GeneratedPasswordDto;
import org.repin.dto.response_dto.GenericTableDataDto;
import org.repin.model.Faculty;
import org.repin.model.Student;
import org.repin.model.StudentGroup;
import org.repin.repository.DeanStaffRepository;
import org.repin.repository.StudentGroupsRepository;
import org.repin.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class DeanStaffController {

    private final StudentGroupsRepository studentGroupsRepository;
    private final StudentRepository studentRepository;
    private final DeanStaffRepository deanStaffRepository;

    @Autowired
    DeanStaffController(StudentGroupsRepository studentGroupsRepository,
                        StudentRepository studentRepository,
                        DeanStaffRepository deanStaffRepository){
        this.studentGroupsRepository = studentGroupsRepository;
        this.studentRepository = studentRepository;
        this.deanStaffRepository = deanStaffRepository;
    }

    @GetMapping("/get_students")
    ResponseEntity<Object> getStudents(){
        List<Student> students = studentRepository.findAll();
        List<String> headers = List.of("#", "ФИО", "Группа", "Почта");
        return ResponseEntity.ok().body(new GenericTableDataDto<Student>(headers, students));
    }

    @PostMapping("add_student")
    ResponseEntity<Object> addStudentAndGenerateDto(@Valid @RequestBody StudentDto dto){
            Student student = new Student(dto.getName(),
                    dto.getStudentGroup(),
                    UUID.randomUUID()
                            .toString()
                            .substring(0, 8));
        return ResponseEntity.ok().body(new GeneratedPasswordDto(studentRepository.save(student).getPassword())); //сохраняем сущность и возвращаем пароль
    }


    @GetMapping("/get_groups")
    public ResponseEntity<GenericTableDataDto<StudentGroup>> getStudentGroups(@RequestParam("userId") UUID deanStaffId) {

        UUID facultyId = deanStaffRepository.findFacultyByStaffId(deanStaffId)
                .orElseThrow(() -> new EntityNotFoundException("Факультет не найден"));

        List<StudentGroup> studentGroups = studentGroupsRepository.findByFacultyId(facultyId);


        List<String> headers = List.of("#", "Название", "Специальность", "Факультет", "Почта группы");
        return ResponseEntity.ok(new GenericTableDataDto<>(headers, studentGroups));
    }

    @PostMapping("/add_group")
    public ResponseEntity<Object> addStudentGroup(@Valid @RequestBody StudentGroupDto dto){
        StudentGroup studentGroup = new StudentGroup(dto.getName(),
                dto.getSpeciality(),
                dto.getFaculty(),
                dto.getEmail());
        return ResponseEntity.ok().body(studentGroupsRepository.save(studentGroup));
    }
}
