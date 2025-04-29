package org.repin.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.repin.dto.request_dto.DisciplineDto;
import org.repin.dto.response_dto.GenericTableDataDto;
import org.repin.model.Discipline;
import org.repin.repository.DeanStaffRepository;
import org.repin.repository.DisciplineRepository;
import org.repin.repository.FacultyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.UUID;

@Service
public class DisciplinesService {

    private final DisciplineRepository disciplineRepository;
    private final DeanStaffRepository deanStaffRepository;
    private final FacultyRepository facultyRepository;

    @Autowired
    DisciplinesService(DisciplineRepository disciplineRepository,
                       DeanStaffRepository deanStaffRepository,
                       FacultyRepository facultyRepository){
        this.disciplineRepository = disciplineRepository;
        this.deanStaffRepository = deanStaffRepository;
        this.facultyRepository = facultyRepository;
    }

    public GenericTableDataDto<Discipline> getDisciplines(UUID deanStaffId) {

//        UUID facultyId = deanStaffRepository.findFacultyByStaffId(deanStaffId)
//                .orElseThrow(() -> new EntityNotFoundException("Факультет не найден"));

        List<Discipline> disciplines = disciplineRepository.findAll();


        List<String> headers = List.of("#", "Название", "Факультет");
        return new GenericTableDataDto<>(headers, disciplines);
    }


    public DisciplineDto addDiscipline(@Valid @RequestBody DisciplineDto dto){
        Discipline discipline = new Discipline(dto.getName(),
                facultyRepository.getReferenceById(dto.getFacultyId()));

        disciplineRepository.save(discipline);

        return dto; //TODO тут не dto Надо возвращать, а сущность. но там пока с этим проблема из-за прокси Faculty
    }
}
