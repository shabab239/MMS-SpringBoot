package com.shabab.mezz.core.repository;

import com.shabab.mezz.core.model.Bill;
import com.shabab.mezz.core.model.Meal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Project: Mezz-SpringBoot
 * Author: Shabab-1281539
 * Created on: 20/10/2024
 */

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {

    Optional<List<Bill>> findByMonthAndYearAndMessId(Integer month, Integer year, Long messId);

}
