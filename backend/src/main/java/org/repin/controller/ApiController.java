package org.repin.controller;

import org.repin.dto.GenericTableDataDto;
import org.repin.model.Faculty;
import org.repin.repository.FacultyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final FacultyRepository facultyRepository;

    @Autowired
    ApiController(FacultyRepository facultyRepository){
        this.facultyRepository = facultyRepository;
    }

    @GetMapping("/faculties")
    ResponseEntity<Object> getFaculties(){
        List<Faculty> faculties = (List<Faculty>) facultyRepository.findAll();
        List<String> headers = List.of("#", "Название", "Адрес");
        return ResponseEntity.ok().body(new GenericTableDataDto<Faculty>(headers, faculties));
    }
}
