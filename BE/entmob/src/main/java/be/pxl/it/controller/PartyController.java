package be.pxl.it.controller;

import be.pxl.it.model.domain.Event;
import be.pxl.it.model.domain.Party;
import be.pxl.it.model.domain.User;
import be.pxl.it.service.PartyService;
import be.pxl.it.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/parties")
public class PartyController {

    private final PartyService partyService;

    private final UserService userService;

    @Autowired
    public PartyController(PartyService partyService, UserService userService) {
        this.partyService = partyService;
        this.userService = userService;
    }

    //(teach only endpoints)

    @GetMapping
    public ResponseEntity getParties(@AuthenticationPrincipal final UserDetails user) {
        return new ResponseEntity<>(partyService.getAll(), HttpStatus.OK);
    }

    @PostMapping
    public HttpStatus addParty(@RequestBody Party party, @AuthenticationPrincipal final UserDetails user) {
        partyService.add(party);
        return HttpStatus.CREATED;
    }

    @DeleteMapping("/{partyId}")
    public HttpStatus deleteParty(@PathVariable("partyId") int partyId, @AuthenticationPrincipal final UserDetails user) {
        partyService.delete(partyId);
        return HttpStatus.OK;
    }

    @PutMapping("/{partyId}")
    public ResponseEntity updateParty(@PathVariable("partyId") int partyId, @RequestBody Party party, @AuthenticationPrincipal final UserDetails user)
    {
        Party updatedParty = partyService.update(party, partyId);
        if(updatedParty != null){
            return new ResponseEntity<>(updatedParty, HttpStatus.OK);
        }
        return  new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{partyId}/members")
    public ResponseEntity updatePartyMembers(@PathVariable("partyId") int partyId, @RequestBody List<User> newUsers, @AuthenticationPrincipal final UserDetails user)
    {
        Party updatedParty = partyService.updateMembers(newUsers, partyId);
        if(updatedParty != null){
            return new ResponseEntity<>(updatedParty, HttpStatus.OK);
        }
        return  new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{partyid}/events")
    public ResponseEntity updatePartyEvents(@PathVariable("partyId") int partyId, @RequestBody List<Event> newEvents, @AuthenticationPrincipal final UserDetails user)
    {
        Party updatedParty = partyService.UpdateEvents(newEvents,partyId);
        if(updatedParty != null){
            return new ResponseEntity<>(updatedParty, HttpStatus.OK);
        }
        return  new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{eventId}/requiredParties")
    public ResponseEntity updateRequiredPartiesOfEvent(@PathVariable("eventId") int eventId, @RequestBody List<Party> requiredParties, @AuthenticationPrincipal final UserDetails user) {
        return new ResponseEntity<>(partyService.updateRequiredPartiesToEvent(eventId, requiredParties), HttpStatus.OK);
    }

    @GetMapping(value = "/{partyId}/members")
    public ResponseEntity getMembersOfParty(@PathVariable("partyId") int partyId, @AuthenticationPrincipal final UserDetails user) {
        return new ResponseEntity<>(partyService.getAllMembers(partyId), HttpStatus.OK);
    }

    @GetMapping(value = "/user/{userId}")
    public ResponseEntity getPartiesOfUser(@PathVariable("userId") int userId, @AuthenticationPrincipal final UserDetails user) {
        return new ResponseEntity<>(userService.getPartiesOfUser(userId), HttpStatus.OK);
    }

}
