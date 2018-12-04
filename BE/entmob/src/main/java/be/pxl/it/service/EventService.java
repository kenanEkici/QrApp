package be.pxl.it.service;

import be.pxl.it.dao.events.EventRepository;
import be.pxl.it.dao.groep.PartyRepository;
import be.pxl.it.dao.users.UserRepository;
import be.pxl.it.model.domain.Event;
import be.pxl.it.model.domain.Party;
import be.pxl.it.model.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class EventService {

    private final EventRepository _eventRepository;

    private final UserRepository _userRepository;

    private final PartyRepository _partyRepository;

    @Autowired
    public EventService(EventRepository _eventRepository, UserRepository _userRepository, PartyRepository _partyRepository) {
        this._eventRepository = _eventRepository;
        this._userRepository = _userRepository;
        this._partyRepository = _partyRepository;
    }

    private Event getById(int id) {
        return _eventRepository.findOne(id);
    }

    public void add(Event event, String cardNumber) {
        User owner = _userRepository.findByCardNumber(cardNumber);
        owner.addCreatedEvent(event);
        _userRepository.save(owner);
    }

    public Event update(int eventId, Event event, String cardNumber) {
        Event foundEvent = _eventRepository.findOne(eventId);
        if (foundEvent != null && foundEvent.getOwner().getCardNumber().equals(cardNumber)) {
            event.setOwner(foundEvent.getOwner());
            return _eventRepository.save(event);
        }
        return null;
    }

    public boolean delete(int eventId, String cardNumber) {
        Event foundEvent = getById(eventId);
        if (foundEvent != null && foundEvent.getOwner().getCardNumber().equals(cardNumber)) {
            //If there is an event and the current user is the owner, we can delete the event
            removeEvent(foundEvent);
            return true;
        }
        return false;
    }

    private void removeEvent(Event foundEvent) {
        foundEvent.removeOwner();
        foundEvent.removeParticipants();
        foundEvent.removeRequiredParties();
        _eventRepository.save(foundEvent);
        _eventRepository.delete(foundEvent);

    }

    public Iterable<Event> getAllEvents() {
        return _eventRepository.findAll();
    }

    public Iterable<Event> getMyEvents(String cardNumber) {
        List<Event> eventList = new ArrayList<>();
        for (Event event : _eventRepository.findAll()) {
            if (Objects.equals(event.getOwner().getCardNumber(), cardNumber))
                eventList.add(event);
        }
        return eventList;
    }

    public Iterable<User> scanParticipant(int eventId, String cardNumber) {
        Event foundEvent = getById(eventId);
        User foundUser = _userRepository.findByCardNumber(cardNumber);

        if (foundEvent != null && foundUser != null) {
            foundUser.addEvent(foundEvent);
            _userRepository.save(foundUser);
            return foundEvent.getParticipants();
        }
        return null;
    }

    public Iterable<User> findParticipantsOfEvent(int eventId) {
        Event foundEvent = getById(eventId);
        if (foundEvent != null)
            return foundEvent.getParticipants();
        return null;
    }

    public Iterable<Event> findParticipationRequiredEvents(String cardNumber) {
        User foundUser = _userRepository.findByCardNumber(cardNumber);
        Set<Party> parties = foundUser.getAssociatedParties();
        List<Event> events = new ArrayList<>();
        for (Party p : parties)
            events.addAll(p.getEvents());
        return events;
    }

    public boolean registerParticipation(int eventId, String cardNumber) {
        Event foundEvent = getById(eventId);
        User foundUser = _userRepository.findByCardNumber(cardNumber);
        if (foundEvent != null && foundUser != null) {
            foundUser.addEvent(foundEvent);
            _userRepository.save(foundUser);
            return true;
        }
        return false;
    }

    public Iterable<Party> findRequiredPartiesOfEvent(int eventId) {
        Event foundEvent = getById(eventId);
        List<Party> requiredParties = new ArrayList<>();
        if (foundEvent != null) {
            requiredParties.addAll(foundEvent.getRequiredParties());
        }
        return requiredParties;
    }
}
