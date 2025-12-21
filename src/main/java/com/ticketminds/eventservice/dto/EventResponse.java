package com.ticketminds.eventservice.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record EventResponse(
        Long id,
        String name,
        String description,
        String location,
        LocalDateTime date,
        BigDecimal price

) {
}
