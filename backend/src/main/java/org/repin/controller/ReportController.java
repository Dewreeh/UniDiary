package org.repin.controller;

import org.repin.dto.response_dto.WeekReportGeneral;
import org.repin.dto.response_dto.WeekReportSubject;
import org.repin.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@RestController("/api")
public class ReportController {

    ReportService reportService;

    @Autowired
    ReportController(ReportService reportService){
        this.reportService = reportService;
    }

    @GetMapping("/get_report")
    ResponseEntity<WeekReportGeneral> getWeekReport(@RequestParam("startDay") LocalDate startDay,
                                                    @RequestParam("endDay") LocalDate endDay,
                                                    @RequestParam("groupId") UUID groupId){

        return ResponseEntity.ok().body(reportService.getWeekReport(startDay, endDay, groupId));
    }

}
