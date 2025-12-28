package com.ticketminds.eventservice.service;

import com.ticketminds.eventservice.dto.EventRequest;
import com.ticketminds.eventservice.dto.EventResponse;
import com.ticketminds.eventservice.model.Event;
import com.ticketminds.eventservice.repository.EventRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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
        // 1. Servise göndereceğimiz DTO (İstek Kutusu)
        EventRequest request = new EventRequest(
                "Tarkan Konseri",
                "Yaz konseri",
                "İstanbul",
                LocalDateTime.now().plusDays(1), // Gelecekte bir tarih
                BigDecimal.valueOf(100)
        );
        // 2. Repository'nin "Kaydettim" diyeceği Entity (Veritabanı Nesnesi)
        // (Repository DTO bilmez, Entity ile konuşur)
        Event savedEvent = Event.builder()
                .id(1L) // ID veritabanından gelir
                .name("Tarkan Konseri")
                .description("Yaz konseri")
                .location("İstanbul")
                .date(request.date())
                .price(BigDecimal.valueOf(100))
                .build();
        // Senaryo: Repo'ya ne gelirse gelsin, bize savedEvent (Entity) dönecek.
        when(eventRepository.save(any(Event.class))).thenReturn(savedEvent);

        // --- WHEN (Eylem) ---
        // Servisi çağırıyoruz. Girdi: Request, Çıktı: Response
        EventResponse result = eventService.createEvent(request);

        // --- THEN (Kontrol) ---
        // 1. Sonuç boş değil mi?
        assertNotNull(result);
        // 2. ID atanmış mı?
        assertEquals(1L, result.id());
        // 3. İsim doğru mu?
        assertEquals("Tarkan Konseri", result.name());

        // 4. Kritik Kontrol: Repository'nin save metodu gerçekten 1 kere çağrıldı mı?
        verify(eventRepository).save(any(Event.class));
    }


    @Test
    void getAllEvents_ShouldReturnResponseList() {
        // --- GIVEN (Hazırlık) ---
        Event event = Event.builder()
                .id(1L)
                .name("Test Event")
                .description("Desc")
                .location("Loc")
                .date(LocalDateTime.now())
                .price(BigDecimal.TEN)
                .build();

        // ÖNEMLİ: Repository'nin döneceği veriyi "Sayfa" (Page) formatına sokuyoruz.
        // PageImpl, List'i alır ve Page gibi davranmasını sağlar.
        org.springframework.data.domain.Page<Event> eventPage = new org.springframework.data.domain.PageImpl<>(List.of(event));

        // Senaryo: Repo'ya "Sayfalı getiri" (findAll(Pageable)) sorulursa, bu sayfayı dön.
        when(eventRepository.findAll(any(org.springframework.data.domain.Pageable.class))).thenReturn(eventPage);

        // --- WHEN (Eylem) ---
        // Artık parametreleri gönderiyoruz: Sayfa 0, Boyut 10, Sıralama 'name', Yön 'ASC'
        org.springframework.data.domain.Page<EventResponse> result = (org.springframework.data.domain.Page<EventResponse>) eventService.getAllEvents(0, 10, "name", "ASC");

        // --- THEN (Kontrol) ---
        assertNotNull(result);
        assertEquals(1, result.getTotalElements()); // Toplam eleman sayısı 1 mi?
        assertEquals("Test Event", result.getContent().get(0).name()); // Listenin ilk elemanının adı doğru mu?
    }
}