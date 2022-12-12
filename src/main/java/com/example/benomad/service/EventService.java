package com.example.benomad.service;

import com.example.benomad.dto.EventDTO;
import com.example.benomad.dto.MessageResponse;
import com.example.benomad.entity.Event;

import java.util.List;

public interface EventService {
    EventDTO getEventById(Long eventId);
    List<EventDTO> getAllEvents();
    EventDTO insertEvent(EventDTO eventDTO);
    EventDTO updateEventById(Long eventId, EventDTO eventDTO);
    EventDTO deleteEventById(Long eventId);
    Event getEventEntityById(Long eventId);
}
