package org.repin.controller;
import org.repin.dto.request_dto.AttendanceRecordDto;
import org.repin.dto.response_dto.AttendanceFormResponse;
import org.repin.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;


@RestController
@RequestMapping("/api")
public class AttendanceController {

    private final AttendanceService attendanceService;

    @Autowired
    AttendanceController(AttendanceService attendanceService){
        this.attendanceService = attendanceService;
    }

    @PostMapping("/mark_attendance")
    ResponseEntity<Object> markAttendance(@RequestBody AttendanceRecordDto attendanceDto){
        attendanceService.markAttendance(attendanceDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/get_attendance_for_schedule")
    ResponseEntity<AttendanceFormResponse> getAttendance(@RequestParam("scheduleId") UUID scheduleId,
                                                         @RequestParam("groupId") UUID groupId){

        return ResponseEntity.ok().body(attendanceService.getAttendanceInfo(groupId, scheduleId));
    }

}
