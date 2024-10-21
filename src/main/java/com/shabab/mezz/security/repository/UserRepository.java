package com.shabab.mezz.security.repository;

import com.shabab.mezz.security.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

/**
 * Project: Mezz-SpringBoot
 * Author: Shabab-1281539
 * Created on: 20/10/2024
 */

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findById(Long userId);

    Optional<User> findByCell(String cell);

    Optional<User> findByIdAndMess_Id(Long userId, Long messId);

    Optional<List<User>> findByMess_Id(Long messId);

}
