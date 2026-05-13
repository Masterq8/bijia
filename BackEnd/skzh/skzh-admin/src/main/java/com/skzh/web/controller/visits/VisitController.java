package com.skzh.web.controller.visits;
import com.skzh.common.core.domain.AjaxResult;


import com.skzh.visits.service.VisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;


//
@RestController
@RequestMapping("/api/visits")
public class VisitController {
    @Autowired
    private VisitService visitService;

    @GetMapping("/daily")
    public AjaxResult getDailyVisits() {
        return AjaxResult.success( visitService.getDailyVisits());
    }
}
