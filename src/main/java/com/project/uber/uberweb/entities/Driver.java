package com.project.uber.uberweb.entities;

import jakarta.persistence.*;
import lombok.*;
import org.locationtech.jts.geom.Point;

import java.math.BigDecimal;


@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(indexes = {
        @Index(name = "idx_driver_vehicle_id", columnList = "vehicleId")
})
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;


    @Column(precision = 3, scale = 2)
    private BigDecimal rating;

    private Boolean available;

    private String vehicleId;

    @Column(columnDefinition = "Geometry(Point, 4326)")
    private Point currentLocation;


}
