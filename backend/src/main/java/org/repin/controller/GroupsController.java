package org.repin.controller;
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
public class GroupsController {

    private final GroupsService groupsService;

    @Autowired
    GroupsController(GroupsService groupsService) {
        this.groupsService = groupsService;
    }


    @GetMapping("/get_groups_by_dean_staff")
    public ResponseEntity<GenericTableDataDto<StudentGroup>> getStudentGroupsByStaff(@RequestParam("userId") UUID deanStaffId) {

        return ResponseEntity.ok(groupsService.getStudentGroupsByStaff(deanStaffId));
    }

    @GetMapping("/get_groups")
    public ResponseEntity<GenericTableDataDto<StudentGroup>> getStudentGroups() {

        return ResponseEntity.ok(groupsService.getStudentGroups());
    }


    @PostMapping("/add_group")
    public ResponseEntity<Object> addStudentGroup(@Valid @RequestBody StudentGroupDto dto) {

        return ResponseEntity.ok().body(groupsService.addStudentGroup(dto));
    }

    @GetMapping("/get_group_id_by_studentId")
    ResponseEntity<UUID> getGroupByStudentID(@RequestParam("studentId") UUID studentId){
        return ResponseEntity.ok().body(groupsService.getGroupIdByStudentId(studentId));
    }

}