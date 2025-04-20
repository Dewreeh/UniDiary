package org.repin.controller;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.repin.dto.request_dto.DisciplineDto;
import org.repin.dto.request_dto.StudentDto;
import org.repin.dto.request_dto.StudentGroupDto;
import org.repin.dto.response_dto.GeneratedPasswordDto;
import org.repin.dto.response_dto.GenericTableDataDto;
import org.repin.model.Discipline;
import org.repin.model.Student;
import org.repin.model.StudentGroup;
import org.repin.repository.*;
import org.repin.service.GroupsService;
import org.repin.service.StudentsService;
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
    private final FacultyRepository facultyRepository;
    private final DisciplineRepository disciplineRepository;
    private final GroupsService groupsService;
    private final StudentsService studentsService;

    @Autowired
    DeanStaffController(StudentGroupsRepository studentGroupsRepository,
                        StudentRepository studentRepository,
                        DeanStaffRepository deanStaffRepository,
                        FacultyRepository facultyRepository,
                        DisciplineRepository disciplineRepository,
                        GroupsService groupsService,
                        StudentsService studentsService){
        this.studentGroupsRepository = studentGroupsRepository;
        this.studentRepository = studentRepository;
        this.deanStaffRepository = deanStaffRepository;
        this.facultyRepository = facultyRepository;
        this.disciplineRepository = disciplineRepository;
        this.groupsService = groupsService;
        this.studentsService = studentsService;
    }

    @GetMapping("/get_students")
    ResponseEntity<GenericTableDataDto<Student>> getStudents(@RequestParam("userId") UUID deanStaffId){

        return ResponseEntity.ok().body(studentsService.getStudents(deanStaffId));
    }

    @PostMapping("add_student")
    ResponseEntity<GeneratedPasswordDto> addStudentAndGeneratePassword(@Valid @RequestBody StudentDto dto){

        return ResponseEntity.ok().body(studentsService.addStudentAndGeneratePassword(dto)); //сохраняем сущность и возвращаем пароль
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
