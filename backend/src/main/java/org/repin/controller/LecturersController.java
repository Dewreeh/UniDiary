package org.repin.controller;
import jakarta.validation.Valid;
import org.repin.dto.request_dto.*;
import org.repin.dto.response_dto.GeneratedPasswordDto;
import org.repin.dto.response_dto.GenericTableDataDto;
import org.repin.model.*;
import org.repin.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class LecturersController {

    private final LecturerService lecturerService;


    @Autowired
    LecturersController(LecturerService lecturerService) {
        this.lecturerService = lecturerService;
    }


    @GetMapping("/get_lecturers")
    ResponseEntity<GenericTableDataDto<Lecturer>> getLecturers() {

        return ResponseEntity.ok().body(lecturerService.getLecturers());
    }

    @PostMapping("/add_lecturer")
    ResponseEntity<GeneratedPasswordDto> addLecturer(@Valid @RequestBody LecturerDto lecturerDto) {

        return ResponseEntity.ok().body(lecturerService.addLecturer(lecturerDto)); //сохраняем сущность и возвращаем пароль
    }
}