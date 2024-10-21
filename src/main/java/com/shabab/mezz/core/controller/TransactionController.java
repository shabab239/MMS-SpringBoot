package com.shabab.mezz.core.controller;

import com.shabab.mezz.core.model.Transaction;
import com.shabab.mezz.core.model.Utility;
import com.shabab.mezz.core.service.TransactionService;
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
@RequestMapping("/api/transaction")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/")
    public ApiResponse getAllTransactions() {
        return transactionService.getAllTransactions();
    }

    @PostMapping("/save")
    public ApiResponse saveTransaction(@Valid @RequestBody Transaction transaction) {
        return transactionService.saveTransaction(transaction);
    }

    @GetMapping("/{id}")
    public ApiResponse getTransactionById(@PathVariable Long id) {
        return transactionService.getTransactionById(id);
    }

    @DeleteMapping("/{id}")
    public ApiResponse deleteTransactionById(@PathVariable Long id) {
        return transactionService.deleteTransactionById(id);
    }
}

