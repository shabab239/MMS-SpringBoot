package com.shabab.mezz.core.service;

import com.shabab.mezz.core.model.Transaction;
import com.shabab.mezz.core.repository.MessRepository;
import com.shabab.mezz.core.repository.TransactionRepository;
import com.shabab.mezz.security.model.Mess;
import com.shabab.mezz.security.model.User;
import com.shabab.mezz.security.repository.UserRepository;
import com.shabab.mezz.util.ApiResponse;
import com.shabab.mezz.util.AuthUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Project: Mezz-SpringBoot
 * Author: Shabab-1281539
 * Created on: 20/10/2024
 */

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private MessRepository messRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional(rollbackOn = Exception.class)
    public ApiResponse saveTransaction(Transaction transaction) {
        ApiResponse response = new ApiResponse();
        try {
            User user = userRepository.findById(
                    transaction.getUser().getId()
            ).orElse(null);
            if (user == null) {
                return response.error("User not found");
            }
            Mess mess = messRepository.findById(AuthUtil.getCurrentMessId()).orElse(null);
            if (mess == null) {
                return response.error("Mess not found");
            }

            if (transaction.getType() == Transaction.TransactionType.DEPOSIT) {
                user.setBalance(user.getBalance() + transaction.getAmount());
                mess.setBalance( mess.getBalance() + transaction.getAmount());
            } else if (transaction.getType() == Transaction.TransactionType.WITHDRAW) {
                if (user.getBalance() < transaction.getAmount() || mess.getBalance() < transaction.getAmount()) {
                    return response.error("Insufficient balance to proceed with the transaction.");
                }
                user.setBalance(user.getBalance() - transaction.getAmount());
                mess.setBalance(mess.getBalance() - transaction.getAmount());
            }

            transaction.setMessId(AuthUtil.getCurrentMessId());
            transactionRepository.save(transaction);
            userRepository.save(user);
            messRepository.save(mess);

            response.setData("transaction", transaction);
            response.success("Transaction created successfully.");
        } catch (Exception e) {
            return response.error(e);
        }
        return response;
    }

    public ApiResponse getTransactionById(Long id) {
        ApiResponse response = new ApiResponse();
        try {
            Transaction transaction = transactionRepository.findByIdAndMessId(
                    id, AuthUtil.getCurrentMessId()
            ).orElse(null);
            if (transaction == null) {
                return response.error("Transaction not found");
            }
            response.setData("transaction", transaction);
            response.success("Transaction fetched successfully.");
        } catch (Exception e) {
            return response.error(e);
        }
        return response;
    }

    public ApiResponse getAllTransactions() {
        ApiResponse response = new ApiResponse();
        try {
            List<Transaction> transactions = transactionRepository.findAllByMessId(
                    AuthUtil.getCurrentMessId()
            ).orElse(new ArrayList<>());
            response.setData("transactions", transactions);
            response.success("Successfully retrieved transactions");
        } catch (Exception e) {
            return response.error(e);
        }
        return response;
    }

    @Transactional(rollbackOn = Exception.class)
    public ApiResponse deleteTransactionById(Long id) {
        ApiResponse response = new ApiResponse();
        try {
            Transaction transaction = transactionRepository.findById(id)
                    .orElse(null);
            if (transaction == null) {
                return response.error("Transaction not found");
            }

            User user = userRepository.findById(transaction.getUser().getId())
                    .orElse(null);
            if (user == null) {
                return response.error("User not found");
            }

            Mess mess = messRepository.findById(AuthUtil.getCurrentMessId())
                    .orElse(null);
            if (mess == null) {
                return response.error("Mess not found");
            }

            if (transaction.getType() == Transaction.TransactionType.DEPOSIT) {
                user.setBalance(user.getBalance() - transaction.getAmount());
                mess.setBalance(mess.getBalance() - transaction.getAmount());
            } else if (transaction.getType() == Transaction.TransactionType.WITHDRAW) {
                user.setBalance(user.getBalance() + transaction.getAmount());
                mess.setBalance(mess.getBalance() + transaction.getAmount());
            }

            userRepository.save(user);
            messRepository.save(mess);

            transactionRepository.deleteById(id);

            response.success("Transaction deleted and balances reverted successfully.");
        } catch (Exception e) {
            return response.error("Error deleting transaction: " + e.getMessage());
        }
        return response;
    }


}
