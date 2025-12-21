package com.ticketminds.eventservice.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record EventRequest(
        @NotBlank(message = "Event name cannot be blank")
        String name,

        String description,

        @NotBlank(message = "Location is mandatory")
        String location,

        @NotNull(message = "Date cannot be null")
        @Future(message = "Event date must be in the future")
        LocalDateTime date,

        @NotNull(message = "Price cannot be null")
        @Min(value=0, message = "Price cannot be negative")
        BigDecimal price

) {
}
