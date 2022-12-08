package com.example.benomad.service;

import com.example.benomad.dto.EventDTO;
import com.example.benomad.dto.MessageResponse;

import java.util.List;

public interface EventService {
    EventDTO getEventById(Long eventId);
    List<EventDTO> getAllEvents();
    EventDTO insertEvent(EventDTO eventDTO);
    EventDTO updateEvent(EventDTO eventDTO);
    MessageResponse deleteEvent(EventDTO eventDTO);
}
