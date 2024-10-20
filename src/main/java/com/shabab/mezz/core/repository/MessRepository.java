package com.shabab.mezz.core.repository;

import com.shabab.mezz.security.model.Mess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Project: Mezz-SpringBoot
 * Author: Shabab-1281539
 * Created on: 20/10/2024
 */

@Repository
public interface MessRepository extends JpaRepository<Mess, Long> {

    Optional<Mess> findById(Long id);

}
