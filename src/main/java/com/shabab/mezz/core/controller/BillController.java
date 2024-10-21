package com.shabab.mezz.core.controller;

import com.shabab.mezz.core.dto.MealDTO;
import com.shabab.mezz.core.service.BillService;
import com.shabab.mezz.core.service.MealService;
import com.shabab.mezz.util.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Project: Mezz-SpringBoot
 * Author: Shabab-1281539
 * Created on: 21/10/2024
 */

@CrossOrigin
@RestController
@RequestMapping("/api/bill")
public class BillController {

    @Autowired
    private BillService billService;

    @PostMapping("/generateBill")
    public ApiResponse getMealsByUserId(
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year
    ) {
        return billService.generateBill(month, year);
    }

    @GetMapping("/")
    public ApiResponse getBills(
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year
    ) {
        return billService.getBills(month, year);
    }

}
