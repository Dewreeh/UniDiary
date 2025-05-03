package org.repin.controller;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.repin.dto.request_dto.AttendanceDto;
import org.repin.dto.response_dto.ConcreteSchedule;
import org.repin.enums.Weekday;
import org.repin.model.Attendance;
import org.repin.service.AttendanceService;
import org.repin.service.SchedulesService;
import org.repin.service.StudentsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController()
@RequestMapping("/api")
public class HeadmanController {

    private final SchedulesService schedulesService;
    private final StudentsService studentsService;
    private final AttendanceService attendanceService;

    HeadmanController(SchedulesService schedulesService,
                      StudentsService studentsService,
                      AttendanceService attendanceService){
        this.schedulesService = schedulesService;
        this.studentsService = studentsService;
        this.attendanceService = attendanceService;
    }

    @GetMapping("/get_concrete_schedules_for_group_by_filters")
    ResponseEntity<List<ConcreteSchedule>> getSchedule(@RequestParam(name="groupId", required = true) UUID groupId,
                                                       @RequestParam(name="weekday", required = false) Weekday weekday,
                                                       @RequestParam(name="lecturerId", required = false) UUID lecturerId,
                                                       @RequestParam(name="disciplineId", required = false) UUID disciplineId){

        return(ResponseEntity.ok().body(schedulesService.getConcreteScheduleForWeekForGroup(groupId, weekday, lecturerId, disciplineId)));
    }

    @PostMapping("/mark_attendance")
    ResponseEntity<List<Attendance>> markAttendance(@RequestBody List<AttendanceDto> attendanceList){
        return ResponseEntity.ok().body(attendanceService.markAttendanceForScheduleItem(attendanceList));
    }
}
