package com.xlight.security.model;

import com.xlight.security.enums.AvailabilityStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "room")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String roomNumber;
    private String description;
    private BigDecimal pricePerNight;
    private Integer maxAdults;
    private Integer maxChildren;

    @Enumerated(EnumType.STRING)
    private AvailabilityStatus availabilityStatus;
}
