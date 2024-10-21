package com.shabab.mezz.core.repository;

import com.shabab.mezz.core.model.Meal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Project: Mezz-SpringBoot
 * Author: Shabab-1281539
 * Created on: 20/10/2024
 */

@Repository
public interface MealRepository extends JpaRepository<Meal, Long> {

    Optional<Meal> findById(Long id);

    Optional<Meal> findByMonthAndYearAndUser_IdAndUser_Mess_Id(Integer month, Integer year, Long userId, Long messId);

    Optional<List<Meal>> findByMonthAndYearAndMessId(Integer month, Integer year, Long messId);

    Optional<List<Meal>> findByUser_IdAndMonthAndYearAndMessId(Long userId, Integer month, Integer year, Long messId);

}
