package com.ticketminds.eventservice.repository;

import com.ticketminds.eventservice.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;


// JpaRepository sayesinde save(), findAll(), findById() gibi metodlar otomatik geldi bile!

public interface EventRepository extends JpaRepository<Event,Long>{


}


