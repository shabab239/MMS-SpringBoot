package com.shabab.mezz.core.service;

import com.shabab.mezz.core.model.Utility;
import com.shabab.mezz.core.repository.MessRepository;
import com.shabab.mezz.core.repository.UtilityRepository;
import com.shabab.mezz.security.model.Mess;
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
public class UtilityService {
    
    @Autowired
    private UtilityRepository utilityRepository;

    @Autowired
    private MessRepository messRepository;

    @Transactional(rollbackOn = Exception.class)
    public ApiResponse saveUtility(Utility utility) {
        ApiResponse response = new ApiResponse();
        try {
            Mess mess = messRepository.findById(
                    AuthUtil.getCurrentMessId()
            ).orElse(null);
            if (mess == null) {
                return response.error("Mess not found");
            }
            if (mess.getBalance() < utility.getCost()) {
                return response.error("Insufficient balance in mess account to pay for the utility.");
            }
            mess.setBalance(mess.getBalance() - utility.getCost());
            utility.setMessId(AuthUtil.getCurrentMessId());
            utilityRepository.save(utility);
            messRepository.save(mess);
            response.setData("utility", utility);
            response.success("Utility created successfully.");
        } catch (Exception e) {
            return response.error(e);
        }
        return response;
    }

    public ApiResponse getUtilityById(Long id) {
        ApiResponse response = new ApiResponse();
        try {
            Utility utility = utilityRepository.findByIdAndMessId(
                    id, AuthUtil.getCurrentMessId()
            ).orElse(null);
            if (utility == null) {
                return response.error("Utility not found");
            }
            response.setData("utility", utility);
            response.success("Utility fetched successfully.");
        } catch (Exception e) {
            return response.error(e);
        }
        return response;
    }

    public ApiResponse getAllUtilities() {
        ApiResponse response = new ApiResponse();
        try {
            List<Utility> utilities = utilityRepository.findAllByMessId(
                    AuthUtil.getCurrentMessId()
            ).orElse(new ArrayList<>());
            response.setData("utilities", utilities);
            response.success("Successfully retrieved utilities");
        } catch (Exception e) {
            return response.error(e);
        }
        return response;
    }

    @Transactional(rollbackOn = Exception.class)
    public ApiResponse deleteUtilityById(Long id) {
        ApiResponse response = new ApiResponse();
        try {
            Utility utility = utilityRepository.findByIdAndMessId(
                    id, AuthUtil.getCurrentMessId()
            ).orElse(null);
            if (utility == null) {
                return response.error("Utility not found");
            }
            Mess mess = messRepository.findById(
                    AuthUtil.getCurrentMessId()
            ).orElse(null);
            if (mess == null) {
                return response.error("Mess not found");
            }
            mess.setBalance(mess.getBalance() + utility.getCost());
            utilityRepository.delete(utility);
            messRepository.save(mess);
            response.success("Utility deleted and balance reverted successfully.");
        } catch (Exception e) {
            return response.error(e);
        }
        return response;
    }


}
