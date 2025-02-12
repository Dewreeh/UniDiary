package org.repin.controller;

import lombok.extern.slf4j.Slf4j;
import org.repin.dto.GenericTableDataDto;
import org.repin.dto.FacultyDto;
import org.repin.model.DeanStaffMember;
import org.repin.model.Faculty;
import org.repin.repository.DeanStaffRepository;
import org.repin.repository.FacultyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@Slf4j
public class ApiController {

    private final FacultyRepository facultyRepository;
    private final DeanStaffRepository deanStaffRepository;

    @Autowired
    ApiController(FacultyRepository facultyRepository,
                  DeanStaffRepository deanStaffRepository){
        this.facultyRepository = facultyRepository;
        this.deanStaffRepository = deanStaffRepository;
    }

    @GetMapping("/get_faculties")
    ResponseEntity<Object> getFaculties(){
        List<Faculty> faculties =  facultyRepository.findAll();
        List<String> headers = List.of("#", "Название", "Почта", "Номер телефона");
        return ResponseEntity.ok().body(new GenericTableDataDto<>(headers, faculties));
    }

    @GetMapping("/get_staff")
    ResponseEntity<Object> getStaff(){
        List<DeanStaffMember> deanStaffMembers = deanStaffRepository.findAll();

        List<String> headers = List.of("#", "ФИО", "Почта", "Факультет");
        return ResponseEntity.ok().body(new GenericTableDataDto<>(headers, deanStaffMembers));
    }

    @PostMapping("add_faculty")
    ResponseEntity<Object> addFaculty(@RequestBody FacultyDto facultyDto){
        log.info("Запрос на API /api/add_faculty с данными: {}", facultyDto);
        Faculty faculty = new Faculty(facultyDto.getName(),
                                        facultyDto.getAddress(),
                                        facultyDto.getPhoneNumber());

        return ResponseEntity.ok().body(facultyRepository.save(faculty));
    }


}
