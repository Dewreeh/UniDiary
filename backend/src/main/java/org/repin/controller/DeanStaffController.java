package org.repin.controller;
import org.repin.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
public class DeanStaffController {

    private final GroupsService groupsService;
    private final StudentsService studentsService;
    private final DisciplinesService disciplinesService;
    private final LecturerService lecturerService;
    private final SchedulesService schedulesService;

    @Autowired
    DeanStaffController(GroupsService groupsService,
                        StudentsService studentsService,
                        DisciplinesService disciplinesService,
                        LecturerService lecturerService,
                        SchedulesService schedulesService){
        this.groupsService = groupsService;
        this.studentsService = studentsService;
        this.disciplinesService = disciplinesService;
        this.lecturerService = lecturerService;
        this.schedulesService = schedulesService;
    }

}
