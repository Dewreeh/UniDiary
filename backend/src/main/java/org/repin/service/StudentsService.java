package org.repin.service;

import jakarta.persistence.EntityNotFoundException;
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


    public GenericTableDataDto<Student> getStudents(UUID deanStaffId){

        UUID facultyId = deanStaffRepository.findFacultyByStaffId(deanStaffId)
                .orElseThrow(() -> new EntityNotFoundException("Факультет не найден"));

        List<Student> students = studentRepository.findByFacultyId(facultyId);

        List<String> headers = List.of("#", "ФИО", "Группа", "Почта");
        return new GenericTableDataDto<Student>(headers, students);
    }

    public GenericTableDataDto<Student> getStudentsByGroup(UUID groupId){


        List<Student> students = studentRepository.findByStudentGroupId(groupId);

        List<String> headers = List.of("#", "ФИО", "Почта");
        return new GenericTableDataDto<Student>(headers, students);
    }

    public Student addStudent(StudentDto dto){

        Student student = new Student(dto.getName(),
                studentGroupsRepository.findById(dto.getStudentGroup()).orElseThrow(),
                null,
                dto.getEmail(),
                false);
        return studentRepository.save(student);
    }

    public void deleteStudent(UUID studentId){

        studentRepository.deleteById(studentId);
    }

    public GenericTableDataDto<Student> getHeadmen(UUID deanStaffId){

        UUID facultyId = deanStaffRepository.findFacultyByStaffId(deanStaffId)
                .orElseThrow(() -> new EntityNotFoundException("Факультет не найден"));

        List<Student> headmen = studentRepository.findHeadmanByFacultyId(facultyId);

        List<String> headers = List.of("#", "ФИО", "Группа", "Почта");

        return new GenericTableDataDto<Student>(headers, headmen);
    }

    public GeneratedPasswordDto promoteStudentToHeadman(UUID studentId){

        Student student = studentRepository.findById(studentId).orElseThrow();

        String generatedPassword = RandomStringUtils.randomAlphanumeric(8);

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();    //TODO вынести в отдельный сервис и сделать уведомления на почту через кафку
        String encodedPassword = encoder.encode(generatedPassword);

        student.setIsHeadman(true);
        student.setPassword(encodedPassword);

        studentRepository.save(student);
        return new GeneratedPasswordDto(encodedPassword); //сохраняем сущность и возвращаем пароль
    }


}
