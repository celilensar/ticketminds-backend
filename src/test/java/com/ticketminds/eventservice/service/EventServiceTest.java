package com.ticketminds.eventservice.service;

import com.ticketminds.eventservice.model.Event;
import com.ticketminds.eventservice.repository.EventRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)  // 1. Mockito kütüphanesini aktif ediyoruz.
class EventServiceTest {

    @Mock // 2. Dublör Repository. Gerçek veritabanına gitmez.
    private EventRepository eventRepository;

    @InjectMocks // 3. Bu dublörü, test edeceğimiz Servisin içine enjekte et.
    private EventService eventService;


    @Test
    void createEvent_ShouldReturnSavedEvent() {

        // --- GIVEN (Hazırlık) ---
        // Kaydedilmek istenen ham veri
        Event eventToSave = Event.builder()
                .name("Tarkan konseri")
                .location("Istanbul")
                .price(BigDecimal.valueOf(100))
                .date(LocalDateTime.now())
                .build();

        // Veritabanından dönmüş gibi yapacağımız (ID'si olan) veri
        Event savedEvent = Event.builder()
                .id(1L) // ID atanmış!
                .name("Tarkan Konseri")
                .location("İstanbul")
                .price(BigDecimal.valueOf(100))
                .date(LocalDateTime.now())
                .build();
        // Senaryoyu kuruyoruz: "Repository'nin save metodu çağrılırsa, savedEvent dön."
        when(eventRepository.save(any(Event.class))).thenReturn(savedEvent);

        // --- WHEN (Eylem) ---
        // Gerçek metodu çağırıyoruz
        Event result = eventService.createEvent(eventToSave);

        // --- THEN (Kontrol) ---
        // 1. Sonuç boş değil mi?
        assertNotNull(result);
        // 2. ID atanmış mı?
        assertEquals(1L, result.getId());
        // 3. İsim doğru mu?
        assertEquals("Tarkan Konseri", result.getName());

        // 4. Kritik Kontrol: Repository'nin save metodu gerçekten 1 kere çağrıldı mı?
        verify(eventRepository).save(any(Event.class));
    }

    @Test
    void getAllEvents() {
    }
}