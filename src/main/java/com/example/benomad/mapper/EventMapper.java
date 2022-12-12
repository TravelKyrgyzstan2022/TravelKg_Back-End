package com.example.benomad.mapper;

import com.example.benomad.dto.EventDTO;
import com.example.benomad.entity.Event;
import com.example.benomad.util.DateUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

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
                .name(eventDTO.getName())
                .address(eventDTO.getAddress())
                .description(eventDTO.getDescription())
                .linkUrl(eventDTO.getLinkUrl())
                .dateTime(DateUtils.parseDateTime(eventDTO.getDateTime(), formatter))
                .build();
    }

    public List<EventDTO> entityListToDtoList(List<Event> entities){
        return entities.stream().map(this::entityToDto).collect(Collectors.toList());
    }
}
