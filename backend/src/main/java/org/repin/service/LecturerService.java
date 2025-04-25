package org.repin.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.repin.dto.request_dto.LecturerDto;
import org.repin.dto.response_dto.GeneratedPasswordDto;
import org.repin.dto.response_dto.GenericTableDataDto;
import org.repin.model.Lecturer;
import org.repin.repository.FacultyRepository;
import org.repin.repository.LecturerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LecturerService {

    private final LecturerRepository lecturerRepository;
    private final FacultyRepository facultyRepository;

    @Autowired
    LecturerService(LecturerRepository lecturerRepository,
                    FacultyRepository facultyRepository){
        this.lecturerRepository = lecturerRepository;
        this.facultyRepository = facultyRepository;
    }


    public GenericTableDataDto<Lecturer> getLecturers(){


        List<Lecturer> lecturers = lecturerRepository.findAll();

        List<String> headers = List.of("#", "ФИО", "Факультет", "Почта");
        return new GenericTableDataDto<Lecturer>(headers, lecturers);
    }

    public GeneratedPasswordDto addLecturer(LecturerDto dto){
        String generatedPassword = RandomStringUtils.randomAlphanumeric(8);

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String encodedPassword = encoder.encode(generatedPassword);

        Lecturer lecturer = new Lecturer(dto.getName(),
                dto.getEmail(),
                facultyRepository.findById(dto.getFacultyId()).orElseThrow(),
                encodedPassword);

        lecturerRepository.save(lecturer);

        return new GeneratedPasswordDto(generatedPassword); //TODO сделать отправку пароля на почту через кафку
    }
}
