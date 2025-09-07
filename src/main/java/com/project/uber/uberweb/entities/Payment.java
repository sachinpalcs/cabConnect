package com.project.uber.uberweb.entities;

import com.project.uber.uberweb.entities.enums.PaymentMethod;
import com.project.uber.uberweb.entities.enums.PaymentStatues;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;


import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @OneToOne(fetch = FetchType.LAZY)
    private Ride ride;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private PaymentStatues paymentStatues;

    @CreationTimestamp
    private LocalDateTime paymentTime;
}
