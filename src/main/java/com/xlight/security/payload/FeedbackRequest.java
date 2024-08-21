package com.xlight.security.payload;

import com.xlight.security.model.Reservation;
import jakarta.validation.constraints.*;
import lombok.Data;



@Data
public class FeedbackRequest {


    @NotNull(message = "Room ID cannot be null")
    private Long roomId;

    @NotEmpty(message = "Name cannot be empty")
    private String name;

    @NotEmpty(message = "Email cannot be empty")
    @Email(message = "Email should be valid")
    private String email;

    @NotEmpty(message = "Content cannot be empty")
    private String content;

    @NotNull(message = "Note cannot be null")
    @Min(value = 1, message = "Note must be between 1 and 5")
    @Max(value = 5, message = "Note must be between 1 and 5")
    private Integer note; // Rating from 1 to 5


}
