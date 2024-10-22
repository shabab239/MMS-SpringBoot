package com.shabab.mezz.core.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.shabab.mezz.security.model.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

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
@Table(name = "core_purchases")
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Description is required")
    @Column(nullable = false)
    String description;

    @NotNull(message = "Cost is required")
    @Column(nullable = false)
    Double cost;

    @NotNull(message = "Date is required")
    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MMM dd, yyyy hh:mm:ss a")
    Date date;

    @NotNull(message = "User is required")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "mess_id", nullable = false)
    private Long messId;

}
