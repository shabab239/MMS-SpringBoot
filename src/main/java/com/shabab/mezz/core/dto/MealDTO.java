package com.shabab.mezz.core.dto;

import com.shabab.mezz.security.model.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Project: Mezz-SpringBoot
 * Author: Shabab-1281539
 * Created on: 20/10/2024
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MealDTO {

    private Long userId;

    private String username;

    private Double meals;

    private Integer day;

    private Integer month;

    private Integer year;

}
