package be.pxl.it.controller;

import be.pxl.it.model.domain.Event;
import be.pxl.it.model.domain.Party;
import be.pxl.it.model.domain.User;
import be.pxl.it.service.EventService;
import be.pxl.it.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events")
public class EventController {

    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    //add an event (teacher-only)
    @PostMapping()
    public HttpStatus addEvent(@RequestBody Event event, @AuthenticationPrincipal final UserDetails user) {
        eventService.add(event, user.getUsername());
        return HttpStatus.CREATED;
    }

    //update an event (teacher and owner-only)
    @PutMapping("/{eventId}")
    public ResponseEntity updateEvent(@PathVariable("eventId") int eventId, @RequestBody Event event, @AuthenticationPrincipal final UserDetails user) {
        Event updatedEvent = eventService.update(eventId, event, user.getUsername());
        if (updatedEvent != null)
            return new ResponseEntity<>(updatedEvent, HttpStatus.OK);
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    //delete an event (teacher and owner-only)
    @DeleteMapping("/{eventId}")
    public HttpStatus deleteEvent(@PathVariable("eventId") int eventId, @AuthenticationPrincipal final UserDetails user) {
        if (eventService.delete(eventId, user.getUsername()))
            return HttpStatus.OK;
        return HttpStatus.NOT_FOUND;
    }

    //get all the events (teacher-only)
    @GetMapping
    public ResponseEntity getAllEvents(@AuthenticationPrincipal final UserDetails user) {
        return new ResponseEntity<>(eventService.getAllEvents(), HttpStatus.OK);
    }

    //get all the events created by the person doing the request (teacher-only)
    @GetMapping("/mine/{cardNumber}")
    public ResponseEntity getMyEvents(@PathVariable("cardNumber") String cardNumber, @AuthenticationPrincipal final UserDetails user) {
        return new ResponseEntity<>(eventService.getMyEvents(cardNumber), HttpStatus.OK);
    }

    //scan users for their participation for a specific event (teacher-only)
    @PostMapping("/{eventId}/{cardNumber}/scan")
    public ResponseEntity scanParticipant(@PathVariable("eventId") int eventId, @PathVariable("cardNumber") String cardNumber, @AuthenticationPrincipal final UserDetails user) {
        Iterable<User> participants = eventService.scanParticipant(eventId, cardNumber);
        if (participants != null)
            return new ResponseEntity<>(participants, HttpStatus.OK);
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    //get the participants of a specific event (teacher-only)
    @GetMapping("/{eventId}/participants")
    public ResponseEntity getParticipantsOfEvent(@PathVariable("eventId") int eventId, @AuthenticationPrincipal final UserDetails user) {
        Iterable<User> participants = eventService.findParticipantsOfEvent(eventId);
        if (participants != null)
            return new ResponseEntity<>(participants, HttpStatus.OK);
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    //get all the events you need to participate (teacher-student)
    @GetMapping("/required/{cardNumber}")
    public ResponseEntity getParticipationRequiredEvents(@PathVariable("cardNumber") String cardNumber, @AuthenticationPrincipal final UserDetails user) {
        return new ResponseEntity<>(eventService.findParticipationRequiredEvents(cardNumber), HttpStatus.OK);
    }

    //Get all the required parties of an event
    @GetMapping("/{eventId}/requiredParties")
    public ResponseEntity getRequiredPartiesOfEvent(@PathVariable("eventId") int eventId, @AuthenticationPrincipal final UserDetails user) {
        Iterable<Party> reqParties = eventService.findRequiredPartiesOfEvent(eventId);
        if (reqParties != null)
            return new ResponseEntity<>(reqParties, HttpStatus.OK);
        return new ResponseEntity(HttpStatus.OK);
    }

    //register a participation at a certain event (teacher-student)
    @PostMapping("/{eventId}")
    public ResponseEntity registerParticipation(@PathVariable("eventId") int eventId, @AuthenticationPrincipal final UserDetails user) {
        if (eventService.registerParticipation(eventId, user.getUsername()))
            return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
