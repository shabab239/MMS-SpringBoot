package com.shabab.mezz.core.service;

import com.shabab.mezz.core.dto.MealDTO;
import com.shabab.mezz.core.model.Meal;
import com.shabab.mezz.core.repository.MealRepository;
import com.shabab.mezz.security.model.User;
import com.shabab.mezz.security.repository.UserRepository;
import com.shabab.mezz.util.ApiResponse;
import com.shabab.mezz.util.AuthUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
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
public class MealService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private MealRepository mealRepository;

    @Autowired
    private UserRepository userRepository;

    public ApiResponse getMealsByUserId(Integer month, Integer year, Long userId) {
        ApiResponse response = new ApiResponse();
        try {
            if (month == null || year == null || userId == null) {
                return response.error("Required params are not present.");
            }

            Long messId = AuthUtil.getCurrentMessId();

            User user = userRepository.findByIdAndMess_Id(
                    userId, messId
            ).orElse(null);

            if (user == null) {
                return response.error("User not found.");
            }

            List<Meal> meals = mealRepository.findByUser_IdAndMonthAndYearAndMessId(
                    userId, month, year, messId
            ).orElse(new ArrayList<>());

            response.setData("meals", meals);
            response.success("Meals fetched successfully");
        } catch (Exception e) {
            return response.error("Error fetching meals: " + e.getMessage());
        }
        return response;
    }

    public ApiResponse getDailyMealRecords(Integer day, Integer month, Integer year) {
        ApiResponse response = new ApiResponse();
        try {
            if (day == null || month == null || year == null) {
                return response.error("Required params are not present.");
            }

            Long messId = AuthUtil.getCurrentMessId();

            List<Meal> meals = mealRepository.findByMonthAndYearAndMessId(
                    month, year, messId
            ).orElse(new ArrayList<>());

            List<User> users = userRepository.findAllByMess_Id(messId).orElse(null);
            if (users == null) {
                return response.error("No users found for this mess.");
            }

            Map<Long, Meal> existingMealRecords = new HashMap<>();
            for (Meal meal : meals) {
                existingMealRecords.put(meal.getUser().getId(), meal);
            }

            for (User user : users) {
                if (!existingMealRecords.containsKey(user.getId())) {
                    Meal newMeal = new Meal();
                    newMeal.setUser(user);
                    newMeal.setMonth(month);
                    newMeal.setYear(year);
                    newMeal.setMessId(messId);
                    mealRepository.save(newMeal);
                }
            }

            if (day < 1 || day > 31) {
                return response.error("Invalid day parameter. It must be between 1 and 31.");
            }

            String dayColumn = "day" + day;

            String queryStr = "SELECT U.id AS userId, U.name AS username, M." + dayColumn + " AS meals, :day AS day, :month AS month, :year AS year " +
                    "FROM core_meals M " +
                    "JOIN sec_users U ON M.user_id = U.id " +
                    "WHERE M.mess_Id = :messId " +
                    "AND U.mess_id = :messId " +
                    "AND M.month = :month " +
                    "AND M.year = :year";

            Query query = entityManager.createNativeQuery(queryStr);
            query.setParameter("day", day);
            query.setParameter("month", month);
            query.setParameter("year", year);
            query.setParameter("messId", messId);

            query.unwrap(NativeQuery.class)
                    .setResultTransformer(Transformers.aliasToBean(MealDTO.class));

            List<MealDTO> dailyMeals = query.getResultList();

            response.setData("meals", dailyMeals);
            response.success("Daily meals fetched successfully");
        } catch (Exception e) {
            return response.error("Error fetching daily meal records: " + e.getMessage());
        }
        return response;
    }

    public ApiResponse recordMeals(List<MealDTO> mealsDTO) {
        ApiResponse response = new ApiResponse();
        try {
            for (MealDTO mealData : mealsDTO) {
                Long userId = mealData.getUserId();
                Double meals = mealData.getMeals();
                Integer day = mealData.getDay();
                Integer month = mealData.getMonth();
                Integer year = mealData.getYear();
                Long messId = AuthUtil.getCurrentMessId();

                if (userId == null || meals == null || day == null || month == null || year == null) {
                    return response.error("Required params are not present.");
                }

                Meal mealRecord = mealRepository.findByMonthAndYearAndUser_IdAndUser_Mess_Id(
                        month, year, userId, messId
                ).orElse(null);

                if (mealRecord == null) {
                    mealRecord = new Meal();
                    mealRecord.setMessId(messId);
                    mealRecord.setUser(new User(userId));
                    mealRecord.setMonth(mealData.getMonth());
                    mealRecord.setYear(mealData.getYear());
                }

                if (day >= 1 && day <= 31) {
                    String methodName = "setDay" + day;
                    Method method = Meal.class.getMethod(methodName, Double.class);
                    method.invoke(mealRecord, meals);
                } else {
                    return response.error("Day must be between 1 and 31.");
                }

                mealRepository.save(mealRecord);
            }

            response.success("Meals recorded successfully");
        } catch (Exception e) {
            return response.error("Error recording meals: " + e.getMessage());
        }
        return response;
    }
}

