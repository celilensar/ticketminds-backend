package com.ticketminds.eventservice.controller;

import org.springframework.data.domain.Page;
import com.ticketminds.eventservice.dto.EventRequest;
import com.ticketminds.eventservice.dto.EventResponse;
import com.ticketminds.eventservice.model.Event;
import com.ticketminds.eventservice.service.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.parser.Entity;
import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @PostMapping  // @Valid: "Request'in içindeki @NotBlank, @Min kurallarını kontrol et" demektir.
    public ResponseEntity<EventResponse> createEvent(@Valid @RequestBody EventRequest request){
        return new ResponseEntity<>(eventService.createEvent(request), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<org.springframework.data.domain.Page<EventResponse>> getAllEvents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String direction
    ) {
        // Service bize Page dönüyor, biz de dışarıya Page dönüyoruz. Uyum tam!
        return ResponseEntity.ok((Page<EventResponse>) eventService.getAllEvents(page, size, sortBy, direction));
    }

}
