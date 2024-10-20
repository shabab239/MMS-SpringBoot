package com.shabab.mezz.core.repository;

import com.shabab.mezz.core.model.Purchase;
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
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

    Optional<Purchase> findByIdAndMessId(Long id, Long messId);

    Optional<List<Purchase>> findAllByMessId(Long messId);


}
