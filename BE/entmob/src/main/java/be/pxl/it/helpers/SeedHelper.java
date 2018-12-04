package be.pxl.it.helpers;

import be.pxl.it.dao.events.EventRepository;
import be.pxl.it.dao.groep.PartyRepository;
import be.pxl.it.dao.users.UserRepository;
import be.pxl.it.model.domain.Event;
import be.pxl.it.model.domain.Party;
import be.pxl.it.model.domain.User;;

import java.util.HashSet;
import java.util.List;

public class SeedHelper {
    private EventRepository _eventRepository;
    private UserRepository _userRepository;
    private PartyRepository _partyRepository;

    public SeedHelper(EventRepository _eventRepository, UserRepository _userRepository, PartyRepository _partyRepository) {
        this._eventRepository = _eventRepository;
        this._userRepository = _userRepository;
        this._partyRepository = _partyRepository;
    }

    public void addParticipantToEvent(User user, Event event){
        event.addParticipant(user);
        saveEventToRepo(event);
        saveUserToRepo(user);
    }

    public void addUserToParty(User user, Party party) {
        user.setSpecificParty(party);
        saveUserToRepo(user);
    }

    public void setOwnerOfEvent(User user, Event event) {
        event.setOwner(user);
        saveUserToRepo(user);
        saveEventToRepo(event);
    }

    public void setEventToParty(Event event, Party party) {
        HashSet<Event> events = new HashSet<>();
        events.add(event);
        party.setEvents(events);
        savePartyToRepo(party);
    }

    public void saveEventToRepo(Event event) {
        _eventRepository.save(event);
    }

    public void saveEventListToRepo(List<Event> eventList) {
        _eventRepository.save(eventList);
    }

    public void saveUserToRepo(User user) {
        _userRepository.save(user);
    }

    public void savePartyToRepo(Party party) {
        _partyRepository.save(party);
    }


}
