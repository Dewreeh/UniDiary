package org.repin.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.repin.dto.response_dto.GeneratedPasswordDto;
import org.repin.dto.response_dto.GenericTableDataDto;
import org.repin.dto.request_dto.FacultyDto;
import org.repin.dto.request_dto.StaffMemberDto;
import org.repin.model.DeanStaffMember;
import org.repin.model.Faculty;
import org.repin.model.Speciality;
import org.repin.repository.DeanStaffRepository;
import org.repin.repository.FacultyRepository;
import org.repin.repository.SpecialityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@Slf4j
public class AdminController {

    private final FacultyRepository facultyRepository;
    private final DeanStaffRepository deanStaffRepository;
    private final SpecialityRepository specialityRepository;

    @Autowired
    AdminController(FacultyRepository facultyRepository,
                    DeanStaffRepository deanStaffRepository,
                    SpecialityRepository specialityRepository){
        this.facultyRepository = facultyRepository;
        this.deanStaffRepository = deanStaffRepository;
        this.specialityRepository = specialityRepository;
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
    ResponseEntity<Object> addFaculty(@Valid @RequestBody FacultyDto facultyDto){
        log.info("Запрос на API /api/add_faculty с данными: {}", facultyDto);
        Faculty faculty = new Faculty(facultyDto.getName(),
                facultyDto.getEmail(),
                facultyDto.getPhoneNumber());
        log.info("Сохранение сущности Faculty: {}", faculty);
        return ResponseEntity.ok().body(facultyRepository.save(faculty));
    }

    @PostMapping("add_staff_member")
    ResponseEntity<Object> addStaffMemberAndGeneratePassword(@Valid @RequestBody StaffMemberDto staffMemberDto){
        String generatedPassword = RandomStringUtils.randomAlphanumeric(8);

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();    //TODO вынести в отдельный сервис и сделать уведомления на почту через кафку
        String encodedPassword = encoder.encode(generatedPassword);

        DeanStaffMember deanStaffMember = new DeanStaffMember(
                staffMemberDto.getName(),
                staffMemberDto.getEmail(),
                staffMemberDto.getFaculty(),
                encodedPassword
        );

        deanStaffRepository.save(deanStaffMember);

        log.info("Сохранение сущности Faculty: {}", staffMemberDto);
        return ResponseEntity.ok().body(new GeneratedPasswordDto(generatedPassword)); //сохраняем сущность и возвращаем пароль
    }

    @GetMapping("/get_specialities")
    ResponseEntity<Object> getSpecialities(){
        List<Speciality> specialities = specialityRepository.findAll();
        List<String> headers = List.of("#", "Название", "Факультет");
        return ResponseEntity.ok().body(new GenericTableDataDto<>(headers, specialities));
    }
}