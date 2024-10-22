package com.shabab.mezz.security.service;

import com.shabab.mezz.core.model.Meal;
import com.shabab.mezz.core.repository.MealRepository;
import com.shabab.mezz.core.repository.MessRepository;
import com.shabab.mezz.security.model.Mess;
import com.shabab.mezz.security.model.User;
import com.shabab.mezz.security.repository.UserRepository;
import com.shabab.mezz.util.ApiResponse;
import com.shabab.mezz.util.AuthUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Project: Mezz-SpringBoot
 * Author: Shabab-1281539
 * Created on: 20/10/2024
 */

@Service
public class UserService implements UserDetailsService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessRepository messRepository;

    @Autowired
    private MealRepository mealRepository;

    public ApiResponse getDashboardInfo() {
        ApiResponse response = new ApiResponse();
        try {
            User user = userRepository.findByIdAndMess_Id(
                    AuthUtil.getCurrentUserId(), AuthUtil.getCurrentMessId()
            ).orElse(null);

            if (user == null) {
                return response.error("User not found");
            }

            Long messId = AuthUtil.getCurrentMessId();
            Integer month = Integer.parseInt(new SimpleDateFormat("MM").format(new Date()));
            Integer year = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));

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

            response.setData("totalPurchaseCost", totalPurchaseCost == null ? 0.0 : totalPurchaseCost);
            response.setData("totalUtilityCost", totalUtilityCost == null ? 0.0 : totalUtilityCost);
            response.setData("totalMeals", totalMeals);
            response.setData("month", new DateFormatSymbols().getMonths()[month - 1]);
            response.setData("mess", user.getMess());

            response.success("Successfully retrieved dashboard info");
        } catch (Exception e) {
            return response.error(e);
        }
        return response;
    }

    public ApiResponse getAllUsers() {
        ApiResponse response = new ApiResponse();
        try {
            List<User> users = userRepository.findAllByMess_Id(
                    AuthUtil.getCurrentMessId()
            ).orElse(new ArrayList<>());
            response.setData("users", users);
            response.success("Successfully retrieved users");
        } catch (Exception e) {
            return response.error(e);
        }
        return response;
    }

    @Transactional(rollbackOn = Exception.class)
    public ApiResponse saveUser(User user) {
        ApiResponse response = new ApiResponse();
        try {
            Mess mess = messRepository.findById(
                    AuthUtil.getCurrentMessId()
            ).orElse(null);
            if (mess == null) {
                return response.error("Mess not found");
            }
            user.setPassword(new BCryptPasswordEncoder(12).encode(user.getPassword()));
            user.setBalance(0.0);
            user.setMess(new Mess(mess.getId()));
            userRepository.save(user);
            response.setData("user", user);
            response.success("User created successfully.");
        } catch (Exception e) {
            return response.error(e);
        }
        return response;
    }

    @Transactional(rollbackOn = Exception.class)
    public ApiResponse updateUser(User updatedUser) {
        ApiResponse response = new ApiResponse();
        try {

            User existingUser = userRepository.findByIdAndMess_Id(
                    updatedUser.getId(), AuthUtil.getCurrentMessId()
            ).orElse(null);
            if (existingUser == null) {
                return response.error("User not found");
            }

            User currentUser = AuthUtil.getCurrentUser();
            if (updatedUser.getRole().equals(User.Role.ROLE_ADMIN) || updatedUser.getRole().equals(User.Role.ROLE_MANAGER)) {
                if (!currentUser.getRole().equals(User.Role.ROLE_ADMIN)) {
                    return response.error("You do not have permission to change user roles.");
                }
            }

            existingUser.setName(updatedUser.getName());
            existingUser.setCell(updatedUser.getCell());
            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setAvatar(updatedUser.getAvatar());
            existingUser.setRole(updatedUser.getRole());
            if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                existingUser.setPassword(new BCryptPasswordEncoder(12).encode(updatedUser.getPassword()));
            }

            userRepository.save(existingUser);
            response.setData("user", existingUser);
            response.success("User updated successfully.");
        } catch (Exception e) {
            return response.error("Error updating user: " + e.getMessage());
        }
        return response;
    }


    @Override
    public UserDetails loadUserByUsername(String cell) throws UsernameNotFoundException {
        User user = userRepository.findByCell(cell).orElse(null);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return user;
    }
}