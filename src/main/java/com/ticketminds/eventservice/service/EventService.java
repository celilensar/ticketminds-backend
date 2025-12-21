package com.ticketminds.eventservice.service;

import com.ticketminds.eventservice.dto.EventRequest;
import com.ticketminds.eventservice.dto.EventResponse;
import com.ticketminds.eventservice.model.Event;
import com.ticketminds.eventservice.repository.EventRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    public EventResponse createEvent(EventRequest request){
        // 1. DTO -> Entity Çevrimi (Manuel Mapping)
        // İleride burası için "MapStruct" kütüphanesi kullanabiliriz.
        Event event= Event.builder()
                .name(request.name())
                .description(request.description())
                .location(request.location())
                .date(request.date())
                .price(request.price())
                .build();
        // 2. Kaydet
        Event savedEvent = eventRepository.save(event);

        // 3. Entity -> DTO Çevrimi (Cevap dönmek için)
        return mapToResponse(savedEvent);
    }

    public List<EventResponse> getAllEvents() {
        return eventRepository.findAll().stream()
                .map(this::mapToResponse) // Her bir event'i response'a çevir
                .toList();
    }
    // Yardımcı metod (Kod tekrarını önlemek için)
    private EventResponse mapToResponse(Event event){
        return new EventResponse(
                event.getId(),
                event.getName(),
                event.getDescription(),
                event.getLocation(),
                event.getDate(),
                event.getPrice()
        );
    }





}
