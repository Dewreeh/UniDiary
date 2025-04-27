package org.repin.service;

import org.repin.dto.request_dto.SemesterDto;
import org.repin.dto.response_dto.GenericTableDataDto;
import org.repin.model.Semester;
import org.repin.repository.SemesterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SemestersService {

    private final SemesterRepository semesterRepository;

    @Autowired
    SemestersService(SemesterRepository semesterRepository){
        this.semesterRepository = semesterRepository;
    }


    public Semester addSemester(SemesterDto dto){

        Semester semester = new Semester();

        semester.setStartDate(dto.getStartDate());
        semester.setEndDate(dto.getEndDate());
        semester.setIsCurrent(true);

        return semesterRepository.save(semester);
    }

    public GenericTableDataDto<Semester> getSemesters(){

        List<String> headers = List.of("#", "Дата начала", "Дата окончания", "Текущий");

        List<Semester> semesters = semesterRepository.findAll();

        return new GenericTableDataDto<Semester>(headers, semesters);
    }
}
