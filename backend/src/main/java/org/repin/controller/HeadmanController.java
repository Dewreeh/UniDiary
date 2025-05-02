package org.repin.controller;

import org.repin.dto.response_dto.ConcreteSchedule;
import org.repin.enums.Weekday;
import org.repin.service.SchedulesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController()
@RequestMapping("/api")
public class HeadmanController {

    private final SchedulesService schedulesService;

    HeadmanController(SchedulesService schedulesService){
        this.schedulesService = schedulesService;
    }

    @GetMapping("/get_concrete_schedules_for_group_by_filters")
    ResponseEntity<List<ConcreteSchedule>> getSchedule(@RequestParam(name="groupId", required = true) UUID groupId,
                                                       @RequestParam(name="weekday", required = false) Weekday weekday,
                                                       @RequestParam(name="lecturerId", required = false) UUID lecturerId,
                                                       @RequestParam(name="disciplineId", required = false) UUID disciplineId){

        return(ResponseEntity.ok().body(schedulesService.getConcreteScheduleForWeekForGroup(groupId, weekday, lecturerId, disciplineId)));
    }
}
