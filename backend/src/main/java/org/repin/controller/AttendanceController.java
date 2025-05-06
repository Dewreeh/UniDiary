package org.repin.controller;
import org.repin.dto.request_dto.AttendanceDto;
import org.repin.dto.request_dto.AttendanceRecordDto;
import org.repin.model.Attendance;
import org.repin.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api")
public class AttendanceController {

    private final AttendanceService attendanceService;

    @Autowired
    AttendanceController(AttendanceService attendanceService){
        this.attendanceService = attendanceService;
    }

    @PostMapping("/mark_attendance")
    ResponseEntity<List<Attendance>> markAttendance(@RequestBody AttendanceRecordDto attendanceDto){

        return ResponseEntity.ok().body(attendanceService.markAttendance(attendanceDto));
    }

}
