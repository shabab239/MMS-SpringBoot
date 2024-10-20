package com.shabab.mezz.core.controller;

import com.shabab.mezz.core.model.Purchase;
import com.shabab.mezz.core.service.PurchaseService;
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
@RequestMapping("/api/purchase")
public class PurchaseController {

    @Autowired
    private PurchaseService purchaseService;

    @GetMapping("/")
    public ApiResponse getAllPurchases() {
        return purchaseService.getAllPurchases();
    }

    @PostMapping("/save")
    public ApiResponse savePurchase(@Valid @RequestBody Purchase purchase) {
        return purchaseService.savePurchase(purchase);
    }

    @GetMapping("/{id}")
    public ApiResponse getPurchaseById(@PathVariable Long id) {
        return purchaseService.getPurchaseById(id);
    }

    @DeleteMapping("/{id}")
    public ApiResponse deletePurchaseById(@PathVariable Long id) {
        return purchaseService.deletePurchaseById(id);
    }


}
