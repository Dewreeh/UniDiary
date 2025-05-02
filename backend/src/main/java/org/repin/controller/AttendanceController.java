package org.repin.controller;
import org.repin.service.SchedulesService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/api")
public class AttendanceController {

    private final SchedulesService schedulesService;

    AttendanceController(SchedulesService schedulesService){
        this.schedulesService = schedulesService;
    }

//    @GetMapping("/get_concrete_schedules_for_group_by_filters")
//    ResponseEntity<List<ConcreteSchedule>> getSchedule(@RequestParam("userId") UUID userId,
//                                                       @RequestParam(name="groupId", required = false) UUID groupId,
//                                                       @RequestParam(name="weekday", required = false) Weekday weekday,
//                                                       @RequestParam(name="lecturerId", required = false) UUID lecturerId,
//                                                       @RequestParam(name="disciplineId", required = false) UUID disciplineId){
//
//        return(ResponseEntity.ok().body(schedulesService.getConcreteScheduleForWeekForGroup(userId, groupId, weekday, lecturerId, disciplineId)));
//    }
}
