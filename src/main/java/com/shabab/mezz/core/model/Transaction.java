package com.shabab.mezz.core.model;

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
@Table(name = "core_transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Date is required")
    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    Date date;

    @NotNull(message = "Amount is required")
    @Column(nullable = false)
    Double amount;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull(message = "Transaction Type is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private Transaction.TransactionType type;

    @Column(name = "mess_id", nullable = false)
    private Long messId;

    public enum TransactionType {
        DEPOSIT,
        WITHDRAW
    }

}
