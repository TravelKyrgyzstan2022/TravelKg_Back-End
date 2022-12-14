package com.example.benomad.controller;

import com.example.benomad.dto.EventDTO;
import com.example.benomad.service.impl.EventServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("api/v1/admin/events")
@Tag(name = "Admin Resource", description = "The Administrator API")
public class AdminEventController {

    private final EventServiceImpl eventService;

    @Operation(summary = "Gets all the events")
    @GetMapping("")
    public ResponseEntity<List<EventDTO>> getAllEvents(){
        return ResponseEntity.ok(eventService.getAllEvents());
    }

    @Operation(summary = "Inserts event")
    @PostMapping("")
    public ResponseEntity<EventDTO> insertEvent(@Valid @RequestBody EventDTO eventDTO){
        return ResponseEntity.ok(eventService.insertEvent(eventDTO));
    }

    @Operation(summary = "Updates event by ID")
    @PutMapping("/{eventId}")
    public ResponseEntity<EventDTO> updateEventById(@PathVariable("eventId") Long eventId,
                                                    @Valid @RequestBody EventDTO eventDTO){
        return ResponseEntity.ok(eventService.updateEventById(eventId, eventDTO));
    }

    @Operation
    @DeleteMapping("/eventId")
    public ResponseEntity<EventDTO> deleteEventById(@PathVariable("eventId") Long eventId){
        return ResponseEntity.ok(eventService.deleteEventById(eventId));
    }
}
