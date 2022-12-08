package com.example.benomad.mapper;

import com.example.benomad.dto.EventDTO;
import com.example.benomad.entity.Event;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class EventMapper {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy 'at' HH:mm");

    public EventDTO entityToDto(Event event){
        return EventDTO.builder()
                .id(event.getId())
                .name(event.getName())
                .description(event.getDescription())
                .address(event.getAddress())
                .imageUrls(event.getImageUrls())
                .linkUrl(event.getLinkUrl())
                .dateTime(formatter.format(event.getDateTime()))
                .build();
    }

    public Event dtoToEntity(EventDTO eventDTO){
        return Event.builder()
                .id(eventDTO.getId())
                .name(eventDTO.getName())
                .address(eventDTO.getAddress())
                .description(eventDTO.getDescription())
                .imageUrls(eventDTO.getImageUrls())
                .linkUrl(eventDTO.getLinkUrl())
                .dateTime(LocalDateTime.parse(eventDTO.getDateTime(), formatter))
                .build();
    }
}
