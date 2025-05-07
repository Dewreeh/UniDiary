package org.repin.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.repin.dto.request_dto.SemesterDto;
import org.repin.dto.response_dto.GeneratedPasswordDto;
import org.repin.dto.response_dto.GenericTableDataDto;
import org.repin.dto.request_dto.FacultyDto;
import org.repin.dto.request_dto.StaffMemberDto;
import org.repin.model.DeanStaffMember;
import org.repin.model.Faculty;
import org.repin.model.Semester;
import org.repin.model.Speciality;
import org.repin.repository.DeanStaffRepository;
import org.repin.repository.FacultyRepository;
import org.repin.repository.SpecialityRepository;
import org.repin.service.DeanStaffService;
import org.repin.service.FacultyService;
import org.repin.service.SemestersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@Slf4j
public class AdminController {

    private final FacultyService facultyService;
    private final DeanStaffService deanStaffService;
    private final SpecialityRepository specialityRepository;
    private final SemestersService semestersService;

    @Autowired
    AdminController(FacultyService facultyService,
                    DeanStaffService deanStaffService,
                    SpecialityRepository specialityRepository,
                    SemestersService semestersService){
        this.facultyService = facultyService;
        this.deanStaffService = deanStaffService;
        this.specialityRepository = specialityRepository;
        this.semestersService = semestersService;
    }

    @GetMapping("/get_faculties")
    ResponseEntity<GenericTableDataDto<Faculty>> getFaculties(){
        GenericTableDataDto<Faculty> faculties =  facultyService.getFaculties();
        return ResponseEntity.ok().body(faculties);
    }

    @PostMapping("add_faculty")
    ResponseEntity<Faculty> addFaculty(@Valid @RequestBody FacultyDto facultyDto){

        return ResponseEntity.ok().body(facultyService.addFaculty(facultyDto));
    }

    @GetMapping("/get_staff")
    ResponseEntity<GenericTableDataDto<DeanStaffMember>> getStaff(){
        GenericTableDataDto<DeanStaffMember> deanStaffMembers = deanStaffService.getStaff();
        return ResponseEntity.ok().body(deanStaffMembers);
    }

    @PostMapping("/add_staff_member")
    ResponseEntity<GeneratedPasswordDto> addStaffMemberAndGeneratePassword(@Valid @RequestBody StaffMemberDto staffMemberDto){

        GeneratedPasswordDto generatedPassword = deanStaffService.addStaffMemberAndGeneratePassword(staffMemberDto);

        log.info("Сохранение сущности Faculty: {}", staffMemberDto);
        return ResponseEntity.ok().body(generatedPassword); //сохраняем сущность и возвращаем пароль
    }

    @GetMapping("/get_specialities")
    ResponseEntity<GenericTableDataDto<Speciality>> getSpecialities(){
        List<Speciality> specialities = specialityRepository.findAll();
        List<String> headers = List.of("#", "Название", "Факультет");
        return ResponseEntity.ok().body(new GenericTableDataDto<>(headers, specialities));
    }

    @GetMapping("/get_semesters")
    ResponseEntity<GenericTableDataDto<Semester>> getSemesters(){

        return ResponseEntity.ok().body(semestersService.getSemesters());
    }

    @PostMapping("/add_semester")
    ResponseEntity<Semester> addSemester(@Valid @RequestBody SemesterDto dto){

        return ResponseEntity.ok().body(semestersService.addSemester(dto));
    }

}