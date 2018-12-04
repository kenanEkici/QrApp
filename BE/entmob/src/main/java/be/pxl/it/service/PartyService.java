package be.pxl.it.service;

import be.pxl.it.dao.events.EventRepository;
import be.pxl.it.dao.groep.PartyRepository;
import be.pxl.it.dao.users.UserRepository;
import be.pxl.it.model.domain.Event;
import be.pxl.it.model.domain.Party;
import be.pxl.it.model.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class PartyService {

    private final PartyRepository _partyRepository;

    private final UserRepository _userRepository;

    private final EventRepository _eventRepository;

    @Autowired
    public PartyService(PartyRepository _partyRepository, UserRepository userRepository, EventRepository eventRepository) {
        this._partyRepository = _partyRepository;
        this._userRepository = userRepository;
        this._eventRepository = eventRepository;
    }

    public boolean add(Party party) {
        Party savedParty = _partyRepository.save(party);
        return (savedParty != null);
    }

    public boolean delete(int partyId) {
        try {
            removeParty(partyId);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public Party update(Party party, int partyId) {
        Party foundParty = _partyRepository.findOne(partyId);
        if (foundParty != null) {
            foundParty.setName(party.getName());
            foundParty.setPartyType(party.getPartyType());
            return _partyRepository.save(foundParty);
        }
        return null;
    }


    public Party UpdateEvents(List<Event> newEvents, int partyId){
        Party foundParty = _partyRepository.findOne(partyId);
        ArrayList<Event> newEventsList = new ArrayList<>(newEvents);
        ArrayList<Event> dbEventsList = new ArrayList<>(foundParty.getEvents());
        //Nieuwe members overlopen
        //Toevoegen indien deze NIET in de DB zitten

        boolean found;
        for(Event newEvent : newEventsList){
            found = false;
            for(Event dbEvent : dbEventsList){
                if(newEvent.getTitle().equals(dbEvent.getTitle())) {
                    found = true;
                    break;
                }
            }
            if(!found){
                Event newEventFromDb = _eventRepository.findOne(newEvent.getId());
                newEventFromDb.addRequiredParty(foundParty);
                _eventRepository.save(newEventFromDb);
            }
        }


        dbEventsList = new ArrayList<>(foundParty.getEvents());
        //DB overlopen
        //Verwijderen indien deze NIET in de newMembers zitten
        for(Event dbEvent : dbEventsList){
            found = false;
            for(Event newEvent : newEventsList){

                if(dbEvent.getTitle().equals(newEvent.getTitle())){
                    found = true;
                    break;
                }
            }
            if(!found){
                Event eventFromDB = _eventRepository.findOne(dbEvent.getId());
                eventFromDB.removeRequiredParty(foundParty.getId());
                _eventRepository.save(eventFromDB);
            }
        }
        return foundParty;
    }


    public Party updateMembers(List<User> newMembers, int partyId){
        Party foundParty = _partyRepository.findOne(partyId);
        ArrayList<User> newMembersList = new ArrayList<>(newMembers);
        ArrayList<User> dbMembersList = new ArrayList<>(foundParty.getMembers());
        //Nieuwe members overlopen
            //Toevoegen indien deze NIET in de DB zitten

        boolean found;
        for(User newUser : newMembers){
            found = false;
            for(User dbUser : dbMembersList){
                if(newUser.getCardNumber().equals(dbUser.getCardNumber())) {
                    found = true;
                    break;
                }
            }
            if(!found){
                foundParty.addMember(_userRepository.findOne(newUser.getId()));
            }
        }
        _partyRepository.save(foundParty);

        dbMembersList = new ArrayList<>(foundParty.getMembers());
        //DB overlopen
            //Verwijderen indien deze NIET in de newMembers zitten
        for(User dbUser : dbMembersList){
            found = false;
            for(User newMember : newMembersList){

                if(dbUser.getCardNumber().equals(newMember.getCardNumber())) {
                    found = true;
                    break;
                }
            }
            if(!found){
                foundParty.removeMembership(dbUser.getId());
            }
        }
        _partyRepository.save(foundParty);
        return foundParty;
    }

    public Iterable<Party> getAll() {
        return _partyRepository.findAll();
    }

    public Iterable<User> getAllMembers(int partyId) {
        Party foundParty = _partyRepository.findOne(partyId);
        if (foundParty != null)
            return foundParty.getMembers();
        return null;
    }

    public void removeParty(int partyID) {

        Party partyToBeRemoved = _partyRepository.findOne(partyID);
        partyToBeRemoved.removeAllRelations();
        _partyRepository.save(partyToBeRemoved);
        _partyRepository.delete(partyToBeRemoved);
    }

    public Event updateRequiredPartiesToEvent(int eventId, List<Party> requiredParties) {
        try {
            Event foundEvent = _eventRepository.findOne(eventId);
            ArrayList<Party> newRequiredPartiesList = new ArrayList<>(requiredParties);
            ArrayList<Party> dbRequiredPartyList = new ArrayList<>(foundEvent.getRequiredParties());

            boolean found;
            for (Party newParty : newRequiredPartiesList) {
                found = false;
                for (Party dbParty : dbRequiredPartyList) {
                    if (newParty.getName().equals(dbParty.getName())) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    foundEvent.addRequiredParty(newParty);
                    _eventRepository.save(foundEvent);
                }
            }
            dbRequiredPartyList = new ArrayList<>(foundEvent.getRequiredParties());

            for (Party dbParty : dbRequiredPartyList) {
                found = false;
                for (Party newRequiredParty : newRequiredPartiesList) {

                    if (dbParty.getName().equals(newRequiredParty.getName())) {
                        found = true;
                        break;
                    }
                }
                if (!found) {

                    foundEvent.removeRequiredParty(dbParty.getId());
                    _eventRepository.save(foundEvent);
                }
            }
            return foundEvent;
        } catch (Exception exception) {
            return null;
        }

    }
}
