package org.repin.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.apache.commons.lang3.RandomStringUtils;
import org.repin.dto.request_dto.StudentDto;
import org.repin.dto.response_dto.GeneratedPasswordDto;
import org.repin.dto.response_dto.GenericTableDataDto;
import org.repin.model.Student;
import org.repin.repository.DeanStaffRepository;
import org.repin.repository.StudentGroupsRepository;
import org.repin.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@Service
public class StudentsService {

    private final StudentRepository studentRepository;
    private final DeanStaffRepository deanStaffRepository;
    private final StudentGroupsRepository studentGroupsRepository;

    @Autowired
    StudentsService(StudentRepository studentRepository,
                    DeanStaffRepository deanStaffRepository,
                    StudentGroupsRepository studentGroupsRepository){
        this.studentRepository = studentRepository;
        this.deanStaffRepository = deanStaffRepository;
        this.studentGroupsRepository = studentGroupsRepository;
    }


    public GenericTableDataDto<Student> getStudents(@RequestParam("userId") UUID deanStaffId){

        UUID facultyId = deanStaffRepository.findFacultyByStaffId(deanStaffId)
                .orElseThrow(() -> new EntityNotFoundException("Факультет не найден"));

        List<Student> students = studentRepository.findByFacultyId(facultyId);

        List<String> headers = List.of("#", "ФИО", "Группа", "Почта");
        return new GenericTableDataDto<Student>(headers, students);
    }

    public GeneratedPasswordDto addStudentAndGeneratePassword(@Valid @RequestBody StudentDto dto){

        String generatedPassword = RandomStringUtils.randomAlphanumeric(8);

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();    //TODO вынести в отдельный сервис и сделать уведомления на почту через кафку
        String encodedPassword = encoder.encode(generatedPassword);

        Student student = new Student(dto.getName(),
                studentGroupsRepository.getReferenceById(dto.getStudentGroup()),
                generatedPassword,
                dto.getEmail());
        return new GeneratedPasswordDto(studentRepository.save(student).getPassword()); //сохраняем сущность и возвращаем пароль
    }
}
