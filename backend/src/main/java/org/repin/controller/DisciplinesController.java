package org.repin.controller;

import io.micrometer.common.lang.Nullable;
import jakarta.validation.Valid;
import org.repin.dto.request_dto.*;
import org.repin.dto.response_dto.GenericTableDataDto;
import org.repin.model.*;
import org.repin.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class DisciplinesController {

    private final DisciplinesService disciplinesService;


    @Autowired
    DisciplinesController(DisciplinesService disciplinesService) {
        this.disciplinesService = disciplinesService;
    }


    @GetMapping("/get_disciplines")
    public ResponseEntity<GenericTableDataDto<Discipline>> getDisciplines(@Nullable @RequestParam("userId") UUID deanStaffId) {

        return ResponseEntity.ok(disciplinesService.getDisciplines(deanStaffId));
    }

    @PostMapping("/add_discipline")
    public ResponseEntity<Object> addDiscipline(@Valid @RequestBody DisciplineDto dto) {

        return ResponseEntity.ok().body(disciplinesService.addDiscipline(dto)); //TODO тут не dto Надо возвращать, а сущность. но там пока с этим проблема из-за прокси Faculty
    }

    @DeleteMapping("/delete_discipline")
    public ResponseEntity<Object> deleteDiscipline(UUID disciplineId) {
        disciplinesService.deleteDiscipline(disciplineId);
        return ResponseEntity.ok().build();
    }

}