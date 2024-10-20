package com.shabab.mezz.core.model;

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
@Entity
@Table(name = "core_meals")
public class Meal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Month is required")
    @Column(name = "month", nullable = false)
    private Integer month;

    @NotNull(message = "Year is required")
    @Column(name = "year", nullable = false)
    private Integer year;

    @NotNull(message = "User is required")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, columnDefinition = "double default 0.0")
    private Double day1;

    @Column(nullable = false, columnDefinition = "double default 0.0")
    private Double day2;

    @Column(nullable = false, columnDefinition = "double default 0.0")
    private Double day3;

    @Column(nullable = false, columnDefinition = "double default 0.0")
    private Double day4;

    @Column(nullable = false, columnDefinition = "double default 0.0")
    private Double day5;

    @Column(nullable = false, columnDefinition = "double default 0.0")
    private Double day6;

    @Column(nullable = false, columnDefinition = "double default 0.0")
    private Double day7;

    @Column(nullable = false, columnDefinition = "double default 0.0")
    private Double day8;

    @Column(nullable = false, columnDefinition = "double default 0.0")
    private Double day9;

    @Column(nullable = false, columnDefinition = "double default 0.0")
    private Double day10;

    @Column(nullable = false, columnDefinition = "double default 0.0")
    private Double day11;

    @Column(nullable = false, columnDefinition = "double default 0.0")
    private Double day12;

    @Column(nullable = false, columnDefinition = "double default 0.0")
    private Double day13;

    @Column(nullable = false, columnDefinition = "double default 0.0")
    private Double day14;

    @Column(nullable = false, columnDefinition = "double default 0.0")
    private Double day15;

    @Column(nullable = false, columnDefinition = "double default 0.0")
    private Double day16;

    @Column(nullable = false, columnDefinition = "double default 0.0")
    private Double day17;

    @Column(nullable = false, columnDefinition = "double default 0.0")
    private Double day18;

    @Column(nullable = false, columnDefinition = "double default 0.0")
    private Double day19;

    @Column(nullable = false, columnDefinition = "double default 0.0")
    private Double day20;

    @Column(nullable = false, columnDefinition = "double default 0.0")
    private Double day21;

    @Column(nullable = false, columnDefinition = "double default 0.0")
    private Double day22;

    @Column(nullable = false, columnDefinition = "double default 0.0")
    private Double day23;

    @Column(nullable = false, columnDefinition = "double default 0.0")
    private Double day24;

    @Column(nullable = false, columnDefinition = "double default 0.0")
    private Double day25;

    @Column(nullable = false, columnDefinition = "double default 0.0")
    private Double day26;

    @Column(nullable = false, columnDefinition = "double default 0.0")
    private Double day27;

    @Column(nullable = false, columnDefinition = "double default 0.0")
    private Double day28;

    @Column(nullable = false, columnDefinition = "double default 0.0")
    private Double day29;

    @Column(nullable = false, columnDefinition = "double default 0.0")
    private Double day30;

    @Column(nullable = false, columnDefinition = "double default 0.0")
    private Double day31;

    @Column(name = "mess_id", nullable = false)
    private Long messId;

}
