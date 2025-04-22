package org.repin.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.repin.dto.request_dto.StudentGroupDto;
import org.repin.dto.response_dto.GenericTableDataDto;
import org.repin.model.StudentGroup;
import org.repin.repository.DeanStaffRepository;
import org.repin.repository.StudentGroupsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.UUID;

@Service
public class GroupsService {

    private final StudentGroupsRepository studentGroupsRepository;
    private final DeanStaffRepository deanStaffRepository;

    @Autowired
    GroupsService(StudentGroupsRepository studentGroupsRepository,
                  DeanStaffRepository deanStaffRepository){
        this.studentGroupsRepository = studentGroupsRepository;
        this.deanStaffRepository = deanStaffRepository;
    }

    public GenericTableDataDto<StudentGroup> getStudentGroups(UUID deanStaffId) {

        UUID facultyId = deanStaffRepository.findFacultyByStaffId(deanStaffId)
                .orElseThrow(() -> new EntityNotFoundException("Факультет не найден"));

        List<StudentGroup> studentGroups = studentGroupsRepository.findByFacultyId(facultyId);


        List<String> headers = List.of("#", "Название", "Специальность", "Факультет", "Почта группы");
        return new GenericTableDataDto<>(headers, studentGroups);
    }

    public StudentGroup addStudentGroup(@Valid @RequestBody StudentGroupDto dto){
        StudentGroup studentGroup = new StudentGroup(dto.getName(),
                dto.getSpeciality(),
                dto.getFaculty(),
                dto.getEmail());
        return studentGroupsRepository.save(studentGroup);
    }



}
