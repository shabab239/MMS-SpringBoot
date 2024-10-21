package com.shabab.mezz.core.controller;

import com.shabab.mezz.core.dto.MealDTO;
import com.shabab.mezz.core.service.MealService;
import com.shabab.mezz.util.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Project: Mezz-SpringBoot
 * Author: Shabab-1281539
 * Created on: 21/10/2024
 */

@CrossOrigin
@RestController
@RequestMapping("/api/meal")
public class MealController {

    @Autowired
    private MealService mealService;

    @GetMapping("/getMealsByUserId")
    public ApiResponse getMealsByUserId(
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Long userId
    ) {
        return mealService.getMealsByUserId(month, year, userId);
    }

    @GetMapping("/getDailyMealRecords")
    public ApiResponse getDailyMealRecords(
            @RequestParam(required = false) Integer day,
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year
    ) {
        return mealService.getDailyMealRecords(day, month, year);
    }

    @PostMapping("/recordMeals")
    public ApiResponse recordMeals(@RequestBody() List<MealDTO> mealsDTO) {
        return mealService.recordMeals(mealsDTO);
    }
}
