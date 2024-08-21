package com.xlight.security.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.xlight.security.enums.AvailabilityStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

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

    @ElementCollection
    @CollectionTable(name = "room_amenities", joinColumns = @JoinColumn(name = "room_id"))
    @Column(name = "amenity")
    private List<String> amenities;

    @ElementCollection
    @CollectionTable(name = "room_services", joinColumns = @JoinColumn(name = "room_id"))
    @Column(name = "service")
    private List<String> roomServices;

    @Column(name = "bed_info")
    private String bedInfo;

    @Column(name = "room_size")
    private String roomSize;

    @Transient
    private String occupancy;

    @JsonIgnore
    @OneToMany(mappedBy = "room")
    private List<Feedback> feedbacks;

    @Column(name = "average_rating")
    private Double averageRating = 0.0;

    // Method to calculate the average rating

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Image mainImage;


    // Method to calculate and return occupancy
    public String getOccupancy() {
        return maxAdults + " adults (" + maxChildren + " children)";
    }
}
