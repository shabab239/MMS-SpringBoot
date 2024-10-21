package com.shabab.mezz.core.service;

import com.shabab.mezz.core.model.Bill;
import com.shabab.mezz.core.model.Meal;
import com.shabab.mezz.core.repository.BillRepository;
import com.shabab.mezz.core.repository.MealRepository;
import com.shabab.mezz.core.repository.PurchaseRepository;
import com.shabab.mezz.core.repository.UtilityRepository;
import com.shabab.mezz.security.model.User;
import com.shabab.mezz.security.repository.UserRepository;
import com.shabab.mezz.util.ApiResponse;
import com.shabab.mezz.util.AuthUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Project: Mezz-SpringBoot
 * Author: Shabab-1281539
 * Created on: 20/10/2024
 */

@Service
public class BillService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MealRepository mealRepository;

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private UtilityRepository utilityRepository;

    @Transactional(rollbackOn = Exception.class)
    public ApiResponse generateBill(Integer month, Integer year) {
        ApiResponse response = new ApiResponse();
        try {
            if (month == null || year == null) {
                return response.error("Required params are not present.");
            }

            Long messId = AuthUtil.getCurrentMessId();

            String purchaseQuery = """
                        SELECT SUM(p.cost) AS total
                        FROM core_purchases p
                        WHERE p.mess_Id = :messId
                        AND MONTH(p.date) = :month
                        AND YEAR(p.date) = :year
                    """;

            Query purchaseQueryObj = entityManager.createNativeQuery(purchaseQuery);
            purchaseQueryObj.setParameter("messId", messId);
            purchaseQueryObj.setParameter("month", month);
            purchaseQueryObj.setParameter("year", year);

            Double totalPurchaseCost = (Double) purchaseQueryObj.getSingleResult();

            String utilityQuery = """
                        SELECT SUM(u.cost) AS total
                        FROM core_utilities u
                        WHERE u.mess_Id = :messId
                        AND u.month = :month
                        AND u.year = :year
                    """;

            Query utilityQueryObj = entityManager.createNativeQuery(utilityQuery);
            utilityQueryObj.setParameter("messId", messId);
            utilityQueryObj.setParameter("month", month);
            utilityQueryObj.setParameter("year", year);

            Double totalUtilityCost = (Double) utilityQueryObj.getSingleResult();

            List<User> users = userRepository.findAllByMess_Id(messId).orElse(null);
            if (users == null) {
                return response.error("No users found for this mess.");
            }

            Double utilityCostPerHead = totalUtilityCost / users.size();

            List<Meal> meals = mealRepository.findByMonthAndYearAndMessId(
                    month, year, messId
            ).orElse(null);
            if (meals == null) {
                return response.error("No meal records found for this month and year.");
            }

            double totalMeals = 0.0;
            Map<Long, Double> userMeals = new HashMap<>();

            for (Meal meal : meals) {
                Long userId = meal.getUser().getId();
                double userTotalMeals = 0;

                for (int day = 1; day <= 31; day++) {
                    String dayMethodName = "getDay" + day;
                    Method method = Meal.class.getMethod(dayMethodName);
                    Double dayMealCount = (Double) method.invoke(meal);
                    userTotalMeals += dayMealCount;
                }

                totalMeals += userTotalMeals;
                userMeals.put(userId, userMeals.getOrDefault(userId, 0.0) + userTotalMeals);
            }

            if (totalMeals == 0.0) {
                return response.error("No meal records found for this month and year.");
            }

            Double costPerMeal = totalPurchaseCost / totalMeals;

            List<Bill> bills = new ArrayList<>();
            List<User> finalUsers = new ArrayList<>();
            for (User user : users) {
                Double mealsEaten = userMeals.get(user.getId());
                Double mealCost = mealsEaten * costPerMeal;
                Double totalBill = mealCost + utilityCostPerHead;

                Bill bill = new Bill();
                bill.setMonth(month);
                bill.setYear(year);
                bill.setMealCost(mealCost);
                bill.setUtilityCost(utilityCostPerHead);
                bill.setFinalAmount(totalBill);
                bill.setUser(user);
                bill.setMessId(messId);

                bills.add(bill);

                user.setBalance(user.getBalance() - totalBill);
                finalUsers.add(user);
            }

            if (!bills.isEmpty()) {
                billRepository.saveAll(bills);
            }

            if (!finalUsers.isEmpty()) {
                userRepository.saveAll(finalUsers);
            }

            //TODO - Auto generate bill, track bill process month and not allow re-process cuz of balance changes of users

            response.setData("userBills", bills);
            response.success("Bills calculated and saved successfully.");
        } catch (Exception e) {
            return response.error("Error fetching bills: " + e.getMessage());
        }
        return response;
    }

    public ApiResponse getBills(Integer month, Integer year) {
        ApiResponse response = new ApiResponse();
        try {
            if (month == null || year == null) {
                return response.error("Required params are not present.");
            }

            Long messId = AuthUtil.getCurrentMessId();

            List<Bill> bills = billRepository.findByMonthAndYearAndMessId(
                    month, year, messId
            ).orElse(new ArrayList<>());

            response.setData("bills", bills);
            response.success("Bills fetched successfully");
        } catch (Exception e) {
            return response.error("Error fetching bills: " + e.getMessage());
        }
        return response;
    }

}
