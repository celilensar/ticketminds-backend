package com.ticketminds.eventservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity // 1. Bu sınıfın veritabanında bir tabloya karşılık geldiğini söyler.
@Table(name = "events") // 2. Tablo adını özelleştiririz (Opsiyonel ama iyi pratiktir).
@Data  // 3. Getter, Setter, toString metodlarını otomatik oluşturur (Lombok).
@NoArgsConstructor  // 4. Parametresiz boş constructor oluşturur (JPA için şarttır).
@AllArgsConstructor // 5. Tüm alanları içeren constructor oluşturur.
@Builder // 6. Nesne oluştururken okunabilirliği artırır (Design Pattern).
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private LocalDateTime date;

    @Column(nullable = false)
    private BigDecimal price;

}
