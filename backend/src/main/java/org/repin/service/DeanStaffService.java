package org.repin.service;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.repin.dto.request_dto.StaffMemberDto;
import org.repin.dto.response_dto.GeneratedPasswordDto;
import org.repin.dto.response_dto.GenericTableDataDto;
import org.repin.model.DeanStaffMember;
import org.repin.repository.DeanStaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
@Slf4j
public class DeanStaffService {

    private final DeanStaffRepository deanStaffRepository;

    @Autowired
    DeanStaffService(DeanStaffRepository deanStaffRepository){
        this.deanStaffRepository = deanStaffRepository;
    }

    public GenericTableDataDto<DeanStaffMember> getStaff(){
        List<DeanStaffMember> deanStaffMembers = deanStaffRepository.findAll();
        List<String> headers = List.of("#", "ФИО", "Почта", "Факультет");
        return new GenericTableDataDto<>(headers, deanStaffMembers);
    }

    public GeneratedPasswordDto addStaffMemberAndGeneratePassword(StaffMemberDto staffMemberDto){
        String generatedPassword = RandomStringUtils.randomAlphanumeric(8);

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();    //TODO вынести в отдельный сервис и сделать уведомления на почту через кафку
        String encodedPassword = encoder.encode(generatedPassword);

        DeanStaffMember deanStaffMember = new DeanStaffMember(
                staffMemberDto.getName(),
                staffMemberDto.getEmail(),
                staffMemberDto.getFaculty(),
                encodedPassword
        );

        deanStaffRepository.save(deanStaffMember);

        log.info("Сохранение сущности Faculty: {}", staffMemberDto);
        return new GeneratedPasswordDto(generatedPassword); //сохраняем сущность и возвращаем пароль
    }
}
