package org.repin.controller;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.apache.commons.lang3.RandomStringUtils;
import org.repin.dto.request_dto.DisciplineDto;
import org.repin.dto.request_dto.StudentDto;
import org.repin.dto.request_dto.StudentGroupDto;
import org.repin.dto.response_dto.GeneratedPasswordDto;
import org.repin.dto.response_dto.GenericTableDataDto;
import org.repin.model.Discipline;
import org.repin.model.Student;
import org.repin.model.StudentGroup;
import org.repin.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class DeanStaffController {

    private final StudentGroupsRepository studentGroupsRepository;
    private final StudentRepository studentRepository;
    private final DeanStaffRepository deanStaffRepository;
    private final FacultyRepository facultyRepository;
    private final DisciplineRepository disciplineRepository;

    @Autowired
    DeanStaffController(StudentGroupsRepository studentGroupsRepository,
                        StudentRepository studentRepository,
                        DeanStaffRepository deanStaffRepository,
                        FacultyRepository facultyRepository,
                        DisciplineRepository disciplineRepository){
        this.studentGroupsRepository = studentGroupsRepository;
        this.studentRepository = studentRepository;
        this.deanStaffRepository = deanStaffRepository;
        this.facultyRepository = facultyRepository;
        this.disciplineRepository = disciplineRepository;
    }

    @GetMapping("/get_students")
    ResponseEntity<Object> getStudents(@RequestParam("userId") UUID deanStaffId){

        UUID facultyId = deanStaffRepository.findFacultyByStaffId(deanStaffId)
                .orElseThrow(() -> new EntityNotFoundException("Факультет не найден"));


        List<Student> students = studentRepository.findByFacultyId(facultyId);

        List<String> headers = List.of("#", "ФИО", "Группа", "Почта");
        return ResponseEntity.ok().body(new GenericTableDataDto<Student>(headers, students));
    }

    @PostMapping("add_student")
    ResponseEntity<Object> addStudentAndGenerateDto(@Valid @RequestBody StudentDto dto){
        String generatedPassword = RandomStringUtils.randomAlphanumeric(8);

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();    //TODO вынести в отдельный сервис и сделать уведомления на почту через кафку
        String encodedPassword = encoder.encode(generatedPassword);

            Student student = new Student(dto.getName(),
                    studentGroupsRepository.getReferenceById(dto.getStudentGroup()),
                    generatedPassword,
                    dto.getEmail());
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

    @GetMapping("/get_disciplines")
    public ResponseEntity<GenericTableDataDto<Discipline>> getDisciplines(@RequestParam("userId") UUID deanStaffId) {

        UUID facultyId = deanStaffRepository.findFacultyByStaffId(deanStaffId)
                .orElseThrow(() -> new EntityNotFoundException("Факультет не найден"));

        List<Discipline> disciplines = disciplineRepository.findByFacultyId(facultyId);


        List<String> headers = List.of("#", "Название", "Факультет");
        return ResponseEntity.ok(new GenericTableDataDto<>(headers, disciplines));
    }

    @PostMapping("/add_discipline")
    public ResponseEntity<Object> addDiscipline(@Valid @RequestBody DisciplineDto dto){
        Discipline discipline = new Discipline(dto.getName(),
                facultyRepository.getReferenceById(dto.getFacultyId()));

        disciplineRepository.save(discipline);

        return ResponseEntity.ok().body(dto); //TODO тут не dto Надо возвращать, а сущность. но там пока с этим проблема из-за прокси Faculty
    }



}
