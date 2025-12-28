package com.ticketminds.eventservice.service;

import com.ticketminds.eventservice.dto.EventRequest;
import com.ticketminds.eventservice.dto.EventResponse;
import com.ticketminds.eventservice.model.Event;
import com.ticketminds.eventservice.repository.EventRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
@Slf4j
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
                .organizerName(request.organizerName())
                .build();
        // 2. Kaydet
        Event savedEvent = eventRepository.save(event);
        log.info("Eklenen: {}", event.getName());
        // 3. Entity -> DTO Çevrimi (Cevap dönmek için)
        return mapToResponse(savedEvent);
    }

    public Page<EventResponse> getAllEvents(int page, int size, String sortBy, String direction) {
        // 1. Sıralama Yönünü Belirle (ASC: Artan, DESC: Azalan)
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        // 2. Sayfalama Talebini Oluştur (Hangi sayfa, kaç tane, nasıl sıralı?)
        Pageable pageable = PageRequest.of(page, size, sort);

        return (Page<EventResponse>) eventRepository.findAll(pageable)
                .map(this::mapToResponse); // Her bir event'i response'a çevir

    }
    // Yardımcı metod (Kod tekrarını önlemek için)
    private EventResponse mapToResponse(Event event){
        return new EventResponse(
                event.getId(),
                event.getName(),
                event.getDescription(),
                event.getLocation(),
                event.getDate(),
                event.getPrice(),
                event.getOrganizerName()
        );
    }

    // Import gerekirse: import com.ticketminds.eventservice.exception.ResourceNotFoundException;
    // (Henüz bu exception sınıfımız yok, şimdilik RuntimeException kullanalım, sonra düzeltiriz)

    public void deleteEvent(Long id) {
        // 1. Önce kayıt var mı bak
        if (!eventRepository.existsById(id)) {
            throw new RuntimeException("Event not found with id: " + id);
        }
        // 2. Varsa sil
        eventRepository.deleteById(id);
    }

    public EventResponse updateEvent(Long id, EventRequest request) {
        // 1. Kayıt var mı kontrol et
        Event existingEvent = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + id));

        // 2. Mevcut kaydı güncelle (Setter metodlarını kullanıyoruz)
        // Not: Gerçek projelerde burada "Mapper" kütüphanesi kullanılır ama şimdilik manuel yapalım.
        existingEvent.setName(request.name());
        existingEvent.setDescription(request.description());
        existingEvent.setLocation(request.location());
        existingEvent.setDate(request.date());
        existingEvent.setPrice(request.price());
        existingEvent.setOrganizerName(request.name());

        // 3. Güncellenmiş hali kaydet
        Event updatedEvent = eventRepository.save(existingEvent);

        // 4. DTO'ya çevirip dön
        return mapToResponse(updatedEvent);
    }





}
