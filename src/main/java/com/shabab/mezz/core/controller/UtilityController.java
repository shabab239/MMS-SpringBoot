package com.shabab.mezz.core.controller;

import com.shabab.mezz.core.model.Utility;
import com.shabab.mezz.core.service.UtilityService;
import com.shabab.mezz.util.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Project: Mezz-SpringBoot
 * Author: Shabab-1281539
 * Created on: 20/10/2024
 */

@CrossOrigin
@RestController
@RequestMapping("/api/utility")
public class UtilityController {

    @Autowired
    private UtilityService utilityService;

    @GetMapping("/")
    public ApiResponse getAllUtilities() {
        return utilityService.getAllUtilities();
    }

    @PostMapping("/save")
    public ApiResponse saveUtility(@Valid @RequestBody Utility utility) {
        return utilityService.saveUtility(utility);
    }

    @GetMapping("/{id}")
    public ApiResponse getUtilityById(@PathVariable Long id) {
        return utilityService.getUtilityById(id);
    }

    @DeleteMapping("/{id}")
    public ApiResponse deleteUtilityById(@PathVariable Long id) {
        return utilityService.deleteUtilityById(id);
    }


}
