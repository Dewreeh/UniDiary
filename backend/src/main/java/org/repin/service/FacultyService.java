package org.repin.service;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.repin.dto.request_dto.FacultyDto;
import org.repin.dto.response_dto.GenericTableDataDto;
import org.repin.model.Faculty;
import org.repin.repository.FacultyRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
@Slf4j
public class FacultyService {

    private final FacultyRepository facultyRepository;

    FacultyService(FacultyRepository facultyRepository){
        this.facultyRepository = facultyRepository;
    }
    public GenericTableDataDto<Faculty> getFaculties(){
        List<Faculty> faculties =  facultyRepository.findAll();
        List<String> headers = List.of("#", "Название", "Почта", "Номер телефона");
        return new GenericTableDataDto<>(headers, faculties);
    }


    public Faculty addFaculty(@Valid @RequestBody FacultyDto facultyDto){
        log.info("Запрос на API /api/add_faculty с данными: {}", facultyDto);
        Faculty faculty = new Faculty(facultyDto.getName(),
                facultyDto.getEmail(),
                facultyDto.getPhoneNumber());
        log.info("Сохранение сущности Faculty: {}", faculty);
        return facultyRepository.save(faculty);
    }
}
