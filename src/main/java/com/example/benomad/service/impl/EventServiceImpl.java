package com.example.benomad.service.impl;

import com.example.benomad.dto.EventDTO;
import com.example.benomad.dto.MessageResponse;
import com.example.benomad.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EventServiceImpl implements EventService {
    @Override
    public EventDTO getEventById(Long eventId) {
        return null;
    }

    @Override
    public List<EventDTO> getAllEvents() {
        return null;
    }

    @Override
    public EventDTO insertEvent(EventDTO eventDTO) {
        return null;
    }

    @Override
    public EventDTO updateEvent(EventDTO eventDTO) {
        return null;
    }

    @Override
    public MessageResponse deleteEvent(EventDTO eventDTO) {
        return null;
    }
}
