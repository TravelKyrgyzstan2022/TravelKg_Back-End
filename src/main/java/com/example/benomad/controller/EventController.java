package com.example.benomad.controller;

import com.example.benomad.dto.EventDTO;
import com.example.benomad.service.impl.EventServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("api/v1/events")
@Tag(name = "Event Resource", description = "The Event API")
public class EventController {

    private final EventServiceImpl eventService;

    @Operation(summary = "Gets all the events")
    @GetMapping("")
    public ResponseEntity<List<EventDTO>> getAllEvents(){
        return ResponseEntity.ok(eventService.getAllEvents());
    }

    @Operation(summary = "Gets event by ID")
    @GetMapping("/{eventId}")
    public ResponseEntity<EventDTO> getEventById(@PathVariable("eventId") Long eventId){
        return ResponseEntity.ok(eventService.getEventById(eventId));
    }
}
