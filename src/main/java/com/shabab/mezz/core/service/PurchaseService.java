package com.shabab.mezz.core.service;

import com.shabab.mezz.core.model.Purchase;
import com.shabab.mezz.core.repository.MessRepository;
import com.shabab.mezz.core.repository.PurchaseRepository;
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
public class PurchaseService {

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private MessRepository messRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional(rollbackOn = Exception.class)
    public ApiResponse savePurchase(Purchase purchase) {
        ApiResponse response = new ApiResponse();
        try {
            User user = userRepository.findByIdAndMess_Id(
                    purchase.getUser().getId(), AuthUtil.getCurrentMessId()
            ).orElse(null);
            if (user == null) {
                return response.error("User not found");
            }
            Mess mess = messRepository.findById(
                    AuthUtil.getCurrentMessId()
            ).orElse(null);
            if (mess == null) {
                return response.error("Mess not found");
            }
            if (mess.getBalance() < purchase.getCost()) {
                return response.error("Insufficient balance in mess account to pay for the purchase.");
            }
            mess.setBalance(mess.getBalance() - purchase.getCost());
            purchase.setMessId(AuthUtil.getCurrentMessId());
            purchaseRepository.save(purchase);
            messRepository.save(mess);
            response.setData("purchase", purchase);
            response.success("Purchase created successfully.");
        } catch (Exception e) {
            return response.error(e);
        }
        return response;
    }

    public ApiResponse getPurchaseById(Long id) {
        ApiResponse response = new ApiResponse();
        try {
            Purchase purchase = purchaseRepository.findByIdAndMessId(
                    id, AuthUtil.getCurrentMessId()
            ).orElse(null);
            if (purchase == null) {
                return response.error("Purchase not found");
            }
            response.setData("purchase", purchase);
            response.success("Purchase fetched successfully.");
        } catch (Exception e) {
            return response.error(e);
        }
        return response;
    }

    public ApiResponse getAllPurchases() {
        ApiResponse response = new ApiResponse();
        try {
            List<Purchase> purchases = purchaseRepository.findAllByMessId(
                    AuthUtil.getCurrentMessId()
            ).orElse(new ArrayList<>());
            response.setData("purchases", purchases);
            response.success("Successfully retrieved purchases");
        } catch (Exception e) {
            return response.error(e);
        }
        return response;
    }

    @Transactional(rollbackOn = Exception.class)
    public ApiResponse deletePurchaseById(Long id) {
        ApiResponse response = new ApiResponse();
        try {
            Purchase purchase = purchaseRepository.findByIdAndMessId(
                    id, AuthUtil.getCurrentMessId()
            ).orElse(null);
            if (purchase == null) {
                return response.error("Purchase not found");
            }
            Mess mess = messRepository.findById(
                    AuthUtil.getCurrentMessId()
            ).orElse(null);
            if (mess == null) {
                return response.error("Mess not found");
            }
            mess.setBalance(mess.getBalance() + purchase.getCost());
            purchaseRepository.delete(purchase);
            messRepository.save(mess);
            response.success("Purchase deleted and balance reverted successfully.");
        } catch (Exception e) {
            return response.error(e);
        }
        return response;
    }


}

