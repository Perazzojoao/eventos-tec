package com.eventostec.api.controller;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.eventostec.api.domain.event.Event;
import com.eventostec.api.domain.event.EventDetailsDTO;
import com.eventostec.api.domain.event.EventRequestDTO;
import com.eventostec.api.domain.event.EventResponseDTO;
import com.eventostec.api.service.EventService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/event")
public class EventController {

  @Autowired
  private EventService eventService;

  @PostMapping(consumes = "multipart/form-data")
  public ResponseEntity<EventResponseDTO> create(@RequestParam("title") String title,
      @RequestParam(value = "description", required = false) String description,
      @RequestParam("date") Long date,
      @RequestParam("city") String city,
      @RequestParam("uf") String uf,
      @RequestParam("remote") Boolean remote,
      @RequestParam("eventUrl") String eventUrl,
      @RequestParam(value = "image", required = false) MultipartFile image) {
    EventRequestDTO eventRequestDTO = new EventRequestDTO(
        title,
        description,
        date,
        city,
        uf,
        remote,
        eventUrl,
        image);

    EventResponseDTO newEvent = this.eventService.createEvent(eventRequestDTO);
    return ResponseEntity.ok(newEvent);
  }

  @GetMapping
  public ResponseEntity<List<EventResponseDTO>> getEventList(@RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size) {
    List<EventResponseDTO> eventList = this.eventService.getUpcomingEvents(page, size);

    return ResponseEntity.ok(eventList);
  }

  @GetMapping("/filter")
  public ResponseEntity<List<EventResponseDTO>> filterEvents(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(required = false) String title,
      @RequestParam(required = false) String city,
      @RequestParam(required = false) String uf,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate) {
    List<EventResponseDTO> eventList = this.eventService.getFilteredEvents(page, size, title, city, uf, startDate,
        endDate);

    return ResponseEntity.ok(eventList);
  }

  @GetMapping("/{id}")
  public ResponseEntity<EventDetailsDTO> getEventDetails(@PathVariable UUID id) {
    EventDetailsDTO event = this.eventService.getEventDetails(id);

    return ResponseEntity.ok(event);
  } 
}
