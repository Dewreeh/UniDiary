package org.repin.controller;

import jakarta.validation.Valid;
import org.repin.dto.request_dto.*;
import org.repin.dto.response_dto.ConcreteSchedule;
import org.repin.dto.response_dto.ScheduleResponseDto;
import org.repin.enums.Weekday;
import org.repin.model.*;
import org.repin.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class SchedulesController {
    private final SchedulesService schedulesService;

    @Autowired
    SchedulesController(SchedulesService schedulesService){
        this.schedulesService = schedulesService;
    }



    @GetMapping("/get_schedules_for_faculty")
    ResponseEntity<List<ScheduleResponseDto>> getSchedules(@RequestParam("userId") UUID userId,
                                                           @RequestParam(name="groupId", required = false) UUID groupId,
                                                           @RequestParam(name="weekday", required = false) Weekday weekday,
                                                           @RequestParam(name="lecturerId", required = false) UUID lecturerId,
                                                           @RequestParam(name="disciplineId", required = false) UUID disciplineId){

        return ResponseEntity.ok().body(schedulesService.getSchedules(
                userId,
                groupId,
                weekday,
                lecturerId,
                disciplineId)
        );
    }

    @PostMapping("/add_schedule_item")
    ResponseEntity<ScheduleItem> addScheduleItem(@Valid @RequestBody ScheduleAddDto dto){

        return ResponseEntity.ok().body(schedulesService.createScheduleItem(dto));
    }

    @GetMapping("/get_schedule_item")
    ResponseEntity<ScheduleResponseDto> getScheduleItem(@RequestParam("scheduleId") UUID scheduleId){

        return ResponseEntity.ok().body(schedulesService.getScheduleItem(scheduleId));
    }


    @DeleteMapping("/delete_schedule_item")
    public ResponseEntity<ScheduleItem> deleteScheduleItem(UUID scheduleItemId){
        schedulesService.deleteScheduleItem(scheduleItemId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/get_concrete_schedules_by_filters")
    ResponseEntity<List<ConcreteSchedule>> getSchedule(@RequestParam(name="groupId", required = false) UUID groupId,
                                                       @RequestParam(name="weekday", required = false) Weekday weekday,
                                                       @RequestParam(name="lecturerId", required = false) UUID lecturerId,
                                                       @RequestParam(name="disciplineId", required = false) UUID disciplineId){

        return(ResponseEntity.ok().body(schedulesService.getConcreteScheduleForWeekForGroup(groupId, weekday, lecturerId, disciplineId)));
    }


}