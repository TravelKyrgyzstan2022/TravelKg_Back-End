package com.example.benomad.service.impl;

import com.example.benomad.dto.EventDTO;
import com.example.benomad.dto.MessageResponse;
import com.example.benomad.entity.Event;
import com.example.benomad.exception.ContentNotFoundException;
import com.example.benomad.mapper.EventMapper;
import com.example.benomad.repository.EventRepository;
import com.example.benomad.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

    @Override
    public EventDTO getEventById(Long eventId) {
        return eventMapper.entityToDto(getEventEntityById(eventId));
    }

    @Override
    public List<EventDTO> getAllEvents() {
        return eventMapper.entityListToDtoList(eventRepository.findAll());
    }

    @Override
    public EventDTO insertEvent(EventDTO eventDTO) {
        return eventMapper.entityToDto(eventRepository.save(eventMapper.dtoToEntity(eventDTO)));
    }

    @Override
    public EventDTO updateEventById(Long eventId, EventDTO eventDTO) {
        Event event = eventMapper.dtoToEntity(eventDTO);
        event.setId(eventId);
        return eventMapper.entityToDto(eventRepository.save(event));
    }

    @Override
    public EventDTO deleteEventById(Long eventId) {
        Event event = getEventEntityById(eventId);
        eventRepository.delete(event);
        return eventMapper.entityToDto(event);
    }

    @Override
    public Event getEventEntityById(Long eventId){
        return eventRepository.findById(eventId).orElseThrow(() -> {
            throw new ContentNotFoundException();
        });
    }
}
