package be.pxl.it.entmob.normal;


import be.pxl.it.EntmobApplication;
import be.pxl.it.dao.events.EventRepository;
import be.pxl.it.dao.groep.PartyRepository;
import be.pxl.it.dao.users.UserRepository;
import be.pxl.it.entmob.TestBuilder;
import be.pxl.it.model.domain.Event;
import be.pxl.it.model.domain.Party;
import be.pxl.it.model.domain.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = EntmobApplication.class)

public class RepositoryIntegrationTest {
    @Autowired
    private EventRepository _eventRepository;
    @Autowired
    private UserRepository _userRepository;
    @Autowired
    private PartyRepository _partyRepository;


    //Repository getters
    @Test
    public void givenEventInDatabase_whenFindByTitle_thenReturnCorrectEvent() throws Exception {

        _eventRepository.deleteAll();
        //given events In Database
        Event testEvent = TestBuilder.event();
        testEvent.setTitle("Particular Test Event");
        _eventRepository.save(testEvent);

        //when find by title
        long countEvents = _eventRepository.count();
        Event result = _eventRepository.findByTitle(testEvent.getTitle());

        //then returns correct event
        assertThat(result).isEqualToComparingFieldByField(testEvent);

    }

    @Test
    public void givenPartyInDatabase_whenFindByName_thenReturnCorrectParty() throws Exception {
        //given events In Database
        Party testParty = TestBuilder.party();
        testParty.setName("Particular Test Name");
        _partyRepository.save(testParty);


        //when find by name
        Party result = _partyRepository.findByName(testParty.getName());

        //then returns correct event
        assertThat(result).isEqualToComparingFieldByField(testParty);

    }

    @Test
    public void addUserToDatabase_whenFindByName_thenReturnCorrectUser() throws Exception {
        //given events In Database
        User testUser = TestBuilder.user();

        _userRepository.save(testUser);

        //when find by name
        User result = _userRepository.findByCardNumber(testUser.getCardNumber());

        //then returns correct event
        assertThat(result).isEqualToComparingFieldByField(testUser);

    }

    @Test
    public void addUserToRepository_WhenFindByCardNumber_ReturnsTheCorrectUser() throws Exception {

        User testUser = TestBuilder.user();
        _userRepository.save(testUser);

        User repoResult = _userRepository.findByCardNumber(testUser.getCardNumber());

        assertThat(repoResult).isEqualToComparingFieldByField(testUser);

    }

    //Relation Testing

    //EVENT
    @Test
    public void addUserToEvent_SeeIfRelationIsCorrect() throws Exception {
        User testUser = TestBuilder.user();
        Event testEvent = TestBuilder.event();

        testEvent.addParticipant(testUser);

        _eventRepository.save(testEvent);

        Set<User> repoParticipantsOfEvent = _eventRepository.findByTitle(testEvent.getTitle()).getParticipants();

        assertThat(testUser).isEqualToIgnoringGivenFields(repoParticipantsOfEvent.iterator().next(), "attendedEvents");
    }

    @Test
    public void addPartyToEvent_WhenFindById_ReturnTheCorrectParty() throws Exception {

        Party testParty = TestBuilder.party();
        testParty.setName("Let's go to the party");
        Event testEvent = TestBuilder.event();
        testEvent.setTitle("Title of Test Event_AddPartyToEvent");
        testEvent.addRequiredParty(testParty);
        Set<Event> newEventsForParty = new HashSet<Event>();
        newEventsForParty.add(testEvent);
        testParty.setEvents(newEventsForParty);
        _partyRepository.save(testParty);
        _eventRepository.save(testEvent);
        Party partyFromEventRepo = _eventRepository.findByTitle(testEvent.getTitle()).getRequiredParties().iterator().next();

        assertThat(testParty).isEqualToIgnoringGivenFields(partyFromEventRepo, "events");
    }

    @Test
    public void addOwnerToEvent_SeeIfRelationIsCorrect() throws Exception {
        //Create Event
        Event testEvent = TestBuilder.event();
        testEvent.setTitle("AddOwnerToEvent");

        //Ceate User
        User testOwner = TestBuilder.user();
        testOwner.setCardNumber(UUID.randomUUID().toString());
        testOwner.setFirstName("Owner");
        //Set User As Owner
        testEvent.setOwner(testOwner);
        //Save in REPO
        _userRepository.save(testOwner);
        _eventRepository.save(testEvent);


        //Get Event From REPO
        Event eventFromRepo = _eventRepository.findByTitle("AddOwnerToEvent");
        User ownerFromRepo = _userRepository.findByCardNumber(testOwner.getCardNumber());
        //See If OwnerFromRepoEvent Is The Same As the TestOwner
        assertThat(testOwner).isEqualToIgnoringGivenFields(ownerFromRepo, "attendedEvents", "associatedParties", "createdEvents");
    }


    //USER
    @Test
    public void addAttendedEventsToUser_SeeIfRelationIsCorrect() throws Exception {

        //CreateUser
        User testUser = TestBuilder.user();
        testUser.setCardNumber(UUID.randomUUID().toString());
        //CreateEvent
        Event testEvent = TestBuilder.event();
        testEvent.setTitle("AddAttendedEventToUser");

        //AddEventToUser
        testUser.addEvent(testEvent);
        testEvent.addParticipant(testUser);

        //Save Relation
        _userRepository.save(testUser);
        _eventRepository.save(testEvent);


        //Check if Attended Event From User is Same

        Set<Event> attendedEventSetFromRepo = _userRepository.findByCardNumber(testUser.getCardNumber()).getAttendedEvents();
        Event attendedEventFromRepo = new Event();
        for (Event e : attendedEventSetFromRepo) {
            if (e.getTitle().equals(testEvent.getTitle()))
                attendedEventFromRepo = e;
        }


        assertThat(testEvent).isEqualToIgnoringGivenFields(attendedEventFromRepo, "participants", "requitedParties");

    }

    @Test
    public void addCreatedEventToUser_SeeIfRelationIsCorrect() throws Exception {

        //Create Event
        Event testEvent = TestBuilder.event();
        testEvent.setTitle("AddCreatedEventToUser");

        //Create User
        User testUser = TestBuilder.user();
        testUser.setCardNumber(UUID.randomUUID().toString());

        //Set user as owner of Event
        testEvent.setOwner(testUser);

        //Add Event to CreatedEvents of User
        testUser.AddCreatedEvents(testEvent);

        //Save relation in DB
        _userRepository.save(testUser);
        _eventRepository.save(testEvent);

        //Get CreatedEvent From User in DB
        Set<Event> createdEventSetFromRepo = _userRepository.findByCardNumber(testUser.getCardNumber()).getCreatedEvents();

        Event eventFromRepo = new Event();
        for (Event e : createdEventSetFromRepo) {
            if (e.getTitle().equals(testEvent.getTitle()))
                eventFromRepo = e;
        }
        //Check if CreatedEvent From DB is equal to testEvent

        assertThat(eventFromRepo).isEqualToIgnoringGivenFields(testEvent, "participants", "requiredParties", "owner");

    }

    @Test
    public void addAssociatedPartiesToUser_SeeIfRelationIsCorrect() throws Exception {
        //Create Party
        Party testParty = TestBuilder.party();
        testParty.setName("addAssociatedPartiesToUser");

        //Create User
        User testUser = TestBuilder.user();
        testUser.setCardNumber(UUID.randomUUID().toString());

        //Set User as Member of Party
        testParty.addMember(testUser);

        //Set Party as AssociatedParty of User
        testUser.addAssociatedParty(testParty);

        //save relation in DB
        _userRepository.save(testUser);
        _partyRepository.save(testParty);

        //Get the associatedParties From the User in the UserDB
        Set<Party> associatedPartiesSetFromRepo = _userRepository.findByCardNumber(testUser.getCardNumber()).getAssociatedParties();

        //Check if the testParty is in the associatedParty.
        Party associatedPartyFromRepo = new Party();
        for (Party p : associatedPartiesSetFromRepo) {
            if (p.getName().equals(testParty.getName())) {
                associatedPartyFromRepo = p;
            }
        }

        //Check If the title of the testParty and the AssociatedParty From the Repo is equal.
        assertThat(testParty).isEqualToIgnoringGivenFields(associatedPartyFromRepo, "members", "events");
    }

    //PARTY
    @Test
    public void addMemberToParty_WhenFindByIDInParty_ReturnsTheCorrectUser() throws Exception {
        User testUser = TestBuilder.user();
        Party testParty = TestBuilder.party();

        testParty.setName("Specific Party Name");
        testUser.setCardNumber(UUID.randomUUID().toString());

        testUser.setSpecificParty(testParty);
        //testParty.addMember(testUser);
        HashSet<User> testUserSet = new HashSet<User>();
        testUserSet.add(testUser);
        testParty.setMembers(testUserSet);

        //_partyRepository.save(testParty);
        _userRepository.save(testUser);

        Party repoParty = _partyRepository.findByName(testParty.getName());
        Set<User> repoMembersOfParty = repoParty.getMembers();
        User repoUser = new User();
        for (int i = 0; i < repoMembersOfParty.size(); i++) {
            User currentUser = repoMembersOfParty.iterator().next();
            if (currentUser.getCardNumber().equals(testUser.getCardNumber())) {
                repoUser = currentUser;
                break;
            }
        }
        assertThat(testUser).isEqualToIgnoringGivenFields(repoUser, "associatedParties");
    }

    @Test
    public void addEventToParty_SeeIfRelationIsCorrect() throws Exception {
        Event testEvent = TestBuilder.event();
        Party testParty = TestBuilder.party();

        testEvent.setTitle("addEventToParty_Event");
        testParty.setName("addEventToParty_Party");

        testParty.addEvent(testEvent);

        _partyRepository.save(testParty);

        Party partyFromRepo = _partyRepository.findByName(testParty.getName());
        Set<Event> eventsFromParty = partyFromRepo.getEvents();

        Event specificEventFromRepo = new Event();
        for (int i = 0; i < eventsFromParty.size(); i++) {
            Event currentEvent = eventsFromParty.iterator().next();
            if (currentEvent.getTitle().equals(testEvent.getTitle())) {
                specificEventFromRepo = currentEvent;
                break;
            }
        }
        assertThat(testEvent).isEqualTo(specificEventFromRepo);
        assertThat(partyFromRepo.getEvents().size()).isEqualTo(1);
    }


    //Update Testing
    @Test
    public void UpdateNameOfPartyInDatabase() throws Exception {

        Party testParty = TestBuilder.party();
        testParty.setName("UpdatePartyTest");


        _partyRepository.save(testParty);
        Party repoParty = _partyRepository.findByName("UpdatePartyTest");
        repoParty.setName("UpdatedPartyTest");
        _partyRepository.save(repoParty);


        Party assertParty = _partyRepository.findByName("UpdatedPartyTest");

        assertThat(assertParty).isEqualTo(repoParty);
    }

    @Test

    public void UpdateMembersOfPartyInDatabase() throws Exception {

        Party testParty = TestBuilder.party();
        testParty.setName("UpdateMembersPartyTest");


        _partyRepository.save(testParty);
        Party repoParty = _partyRepository.findByName("UpdateMembersPartyTest");
        User testUser = TestBuilder.user();

        repoParty.addMember(testUser);
        _partyRepository.save(repoParty);


        Party assertParty = _partyRepository.findByName("UpdateMembersPartyTest");

        assertThat(assertParty).isEqualTo(repoParty);
        assertThat(assertParty.getMembers().size()).isEqualTo(repoParty.getMembers().size());
    }

    @Test
    public void UpdateRequiredPartiesOfEventInDatabase() throws Exception {

        Event testEvent = TestBuilder.event();
        testEvent.setTitle("UpdateRequiredPartiesOfEvent_Event");

        _eventRepository.save(testEvent);

        Event repoEvent = _eventRepository.findByTitle(testEvent.getTitle());

        Party testParty = TestBuilder.party();
        testParty.setName("UpdateRequiredPartiesOfEvent_Party");

        repoEvent.addRequiredParty(testParty);

        _eventRepository.save(repoEvent);

        Event assertEvent = _eventRepository.findByTitle(testEvent.getTitle());

        assertThat(assertEvent).isEqualTo(repoEvent);
        assertThat(assertEvent.getRequiredParties().size()).isEqualTo(repoEvent.getRequiredParties().size());
    }

    @Test
    public void UpdateParticipatingEventsOfUserInDatabase() throws Exception {

        User testUser = TestBuilder.user();
        testUser.setFirstName("UpdateParticipatingEventsOfUser_User");
        testUser.setCardNumber(UUID.randomUUID().toString());
        Event testEvent1 = TestBuilder.event();
        testEvent1.setTitle("testEvent1");
        testUser.addEvent(testEvent1);


        _userRepository.save(testUser);

        Event testEvent2 = TestBuilder.event();
        testEvent2.setTitle("testEvent2");

        User repoUser = _userRepository.findByCardNumber(testUser.getCardNumber());
        repoUser.addEvent(testEvent2);

        _userRepository.save(repoUser);


        User assertUser = _userRepository.findByCardNumber(testUser.getCardNumber());

        assertThat(assertUser).isEqualTo(repoUser);
        assertThat(assertUser.getAttendedEvents().size()).isEqualTo(repoUser.getAttendedEvents().size());


    }

    @Test
    public void UpdateOwningEventsOfUerInDatabase() throws Exception {
        User testUser = TestBuilder.user();
        testUser.setCardNumber(UUID.randomUUID().toString());

        Event testEvent1 = TestBuilder.event();
        testEvent1.setTitle("testEvent1");
        testUser.addCreatedEvent(testEvent1);

        _userRepository.save(testUser);


        User repoUser = _userRepository.findByCardNumber(testUser.getCardNumber());
        Event testEvent2 = TestBuilder.event();
        testEvent2.setTitle("testEvent2");
        repoUser.addCreatedEvent(testEvent2);

        _userRepository.save(repoUser);

        User assertUser = _userRepository.findByCardNumber(testUser.getCardNumber());

        assertThat(assertUser).isEqualTo(repoUser);
        assertThat(assertUser.getCreatedEvents().size()).isEqualTo(repoUser.getCreatedEvents().size());
        assertThat(assertUser.getCreatedEvents().size()).isEqualTo(2);


    }

    //Remove testing
    @Test
    public void RemoveUserWithoutRelationsFromDatabase() throws Exception {

        User testUser = TestBuilder.user();
        testUser.setCardNumber(UUID.randomUUID().toString());

        _userRepository.save(testUser);

        long userCount = _userRepository.count();

        _userRepository.delete(testUser);
        assertThat(_userRepository.count()).isEqualTo(userCount-1);
    }

    @Test
    public void RemoveUserWithParticipatingEventFromDatabase() throws Exception {

        User testUser = TestBuilder.user();
        testUser.setCardNumber(UUID.randomUUID().toString());

        Event testEvent = TestBuilder.event();
        testEvent.setTitle("RemoveUserWithParticipatingEventFromDatabase_Event");

        testUser.addEvent(testEvent);
        _userRepository.save(testUser);

        long eventCount = _eventRepository.count();
        long userCount = _userRepository.count();

        testUser = _userRepository.findByCardNumber(testUser.getCardNumber());
        testUser.removeParticipatingEvent(testEvent.getId());
        testEvent = _eventRepository.findByTitle(testEvent.getTitle());
        testEvent.removeParticipant(testUser.getId());
        _userRepository.save(testUser);
        _eventRepository.save(testEvent);


        //_eventRepository.save(testEvent);
        _userRepository.delete(testUser);

        assertThat(_userRepository.count()).isEqualTo(userCount-1);
        assertThat(_eventRepository.count()).isEqualTo(eventCount);

        Event repoEvent = _eventRepository.findByTitle(testEvent.getTitle());

        assertThat(repoEvent.getParticipants().size()).isEqualTo(0);
    }


    //Removing fully connected entities from the database
    @Test
    public void RemoveUserWithParticipatingEventsAndCreatedEventsAndAssociatedPartiesFromDatabase() throws Exception {
        User testUser = TestBuilder.user();

        Event createdEvent = TestBuilder.event();
        createdEvent.setTitle("CreatedTestEvent");

        Event participatingEvent = TestBuilder.event();
        participatingEvent.setTitle("ParticipatingEvent");

        Party associatedParty = TestBuilder.party();
        associatedParty.setName("AssociatedParty");

        testUser.addEvent(participatingEvent);
        testUser.addCreatedEvent(createdEvent);
        associatedParty.addMember(testUser);

        _userRepository.save(testUser);
        _partyRepository.save(associatedParty);


        long partyCount = _partyRepository.count();
        long eventCount = _eventRepository.count();
        long userCount = _userRepository.count();

        User repoUser = _userRepository.findByCardNumber(testUser.getCardNumber());


        //Remove Ownership
        //GetAllTheCreatedEventIDs
        for (Event e : repoUser.getCreatedEvents()) {
            Event createdEventFromRepo = _eventRepository.findByTitle(e.getTitle());
            createdEventFromRepo.setOwner(null);
            repoUser.removeOwnership(e.getId());
            _userRepository.save(repoUser);
            _eventRepository.save(createdEventFromRepo);
        }

        //Remove the ownership link


        //Remove Participation
        for (Event e : repoUser.getAttendedEvents()) {
            Event attendedEventFromRepo = _eventRepository.findByTitle(e.getTitle());
            attendedEventFromRepo.removeParticipant(repoUser.getId());
            _eventRepository.save(attendedEventFromRepo);
            repoUser.removeParticipatingEvent(e.getId());
        }
        _userRepository.save(repoUser);


        //Remove Membership
        for (Party p : repoUser.getAssociatedParties()) {
            Party partyFromRepo = _partyRepository.findByName(p.getName());
            partyFromRepo.removeMembership(repoUser.getId());
            _partyRepository.save(partyFromRepo);
            repoUser.removeAssociatedParty(partyFromRepo.getId());
            _userRepository.save(repoUser);
        }

        Event createdEventFromRepo = _eventRepository.findByTitle(createdEvent.getTitle());
        Event participatedEventFromRepo = _eventRepository.findByTitle(participatingEvent.getTitle());
        Party membershipPartyFromRepo = _partyRepository.findByName(associatedParty.getName());

        _userRepository.delete(repoUser);


        assertThat(_userRepository.count()).isEqualTo(userCount-1);
        assertThat(_eventRepository.count()).isEqualTo(eventCount);
        assertThat(_partyRepository.count()).isEqualTo(partyCount);

    }

    @Test
    public void RemovePartyWithMembersAndEventsFromDatabase() throws Exception {
        Party testParty = TestBuilder.party();
        testParty.setName("RemovePartyWithMembersAndEvents_Party");

        User testMember = TestBuilder.user();
        testMember.setCardNumber(UUID.randomUUID().toString());
        testParty.addMember(testMember);
        //_partyRepository.save(testParty);

        Event testEvent = TestBuilder.event();
        testEvent.setTitle("RemovePartyWithMembersAndEvents_Event");
        testEvent.addRequiredParty(testParty);
        _eventRepository.save(testEvent);

        long partyCount = _partyRepository.count();
        long eventCount = _eventRepository.count();
        long userCount = _userRepository.count();


        Party repoParty = _partyRepository.findByName(testParty.getName());


        //RemoveMembers
        for (User u : repoParty.getMembers()) {
            u.removeAssociatedParty(repoParty.getId());
            _userRepository.save(u);
            repoParty.removeMembership(u.getId());
        }
        _partyRepository.save(repoParty);

        //RemoveEvents
        for (Event e : repoParty.getEvents()) {
            e.removeRequiredParty(repoParty.getId());
            _eventRepository.save(e);
            repoParty.removeEvent(e.getId());
        }
        _partyRepository.save(repoParty);

        _partyRepository.delete(repoParty);

        assertThat(_partyRepository.count()).isEqualTo(partyCount-1);
        assertThat(_eventRepository.count()).isEqualTo(eventCount);
        assertThat(_userRepository.count()).isEqualTo(userCount);


    }

    @Test
    public void RemoveEventWithOwnerAndParticipantsAndRequiredPartiesFromDatabase() throws Exception {
        Event testEvent = TestBuilder.event();
        testEvent.setTitle("RemoveEventWithOwnerAndParticipantsAndRequitedParties_Event");

        User testParticipant = TestBuilder.user();
        testParticipant.setCardNumber(UUID.randomUUID().toString());

        User testOwner = TestBuilder.user();
        testOwner.setCardNumber(UUID.randomUUID().toString());

        Party testRequiredParty = TestBuilder.party();
        testRequiredParty.setName("RemoveEventWithOwnerAndParticipantsAndRequitedParties_Party");

        testEvent.addRequiredParty(testRequiredParty);

        testOwner.addCreatedEvent(testEvent);
        testParticipant.addEvent(testEvent);

        _userRepository.save(testOwner);
        _userRepository.save(testParticipant);

        long partyCount = _partyRepository.count();
        long eventCount = _eventRepository.count();
        long userCount = _userRepository.count();

        Event repoEvent = _eventRepository.findByTitle(testEvent.getTitle());


        //Remove Owner
        User ownerOfEvent = repoEvent.getOwner();
        ownerOfEvent.removeOwnership(repoEvent.getId());
        repoEvent.setOwner(null);
        _userRepository.save(ownerOfEvent);
        _eventRepository.save(repoEvent);


        //Remove Participants
        for (User u : repoEvent.getParticipants()) {
            u.removeParticipatingEvent(repoEvent.getId());
            _userRepository.save(u);
            repoEvent.removeParticipant(u.getId());
            _eventRepository.save(repoEvent);
        }

        //Remove RequiredParties
        for (Party p : repoEvent.getRequiredParties()) {
            p.removeEvent(repoEvent.getId());
            _partyRepository.save(p);
            repoEvent.removeRequiredParty(p.getId());
            _eventRepository.save(repoEvent);
        }

        _eventRepository.delete(repoEvent);

        Party partyFromRepo = _partyRepository.findByName(testRequiredParty.getName());
        User ownerFromRepo = _userRepository.findByCardNumber(testOwner.getCardNumber());
        User participantFromRepo = _userRepository.findByCardNumber(testParticipant.getCardNumber());


        assertThat(_partyRepository.count()).isEqualTo(partyCount);
        assertThat(_eventRepository.count()).isEqualTo(eventCount-1);
        assertThat(_userRepository.count()).isEqualTo(userCount);
    }


    //TheUltimateTest

    @Test
    public void RemoveEverything() {
        User testOwner = TestBuilder.user();
        testOwner.setCardNumber(UUID.randomUUID().toString());

        User testParticipant = TestBuilder.user();
        testParticipant.setCardNumber(UUID.randomUUID().toString());


        Party testParty = TestBuilder.party();
        testParty.setName("RemoveEverythingParty");

        Event testEvent = TestBuilder.event();
        testEvent.setTitle("RemoveEverythingEvent");


        //ULTIMATE LINKING
        testEvent.addRequiredParty(testParty);
        testOwner.addCreatedEvent(testEvent);
        testOwner.addAssociatedParty(testParty);
        testParty.addMember(testOwner);
        testParty.addMember(testParticipant);
        testParticipant.addEvent(testEvent);
        testParticipant.addAssociatedParty(testParty);

        _userRepository.save(testOwner);
        _userRepository.save(testParticipant);



        //Event eventFromRepo = _eventRepository.findByTitle(testEvent.getTitle());
        //User ownerFromRepo = _userRepository.findByCardNumber(testOwner.getCardNumber());
        //User participantFromRepo = _userRepository.findByCardNumber(testParticipant.getCardNumber());
        //Party partyFromRepo = _partyRepository.findByName(testParty.getName());

        long partyCount = _partyRepository.count();
        long eventCount = _eventRepository.count();
        long userCount = _userRepository.count();

        //ULTIMATE REMOVAL

        //Remove an owner

        User repoUser = _userRepository.findByCardNumber(testOwner.getCardNumber());

        //Remove Ownership
        //GetAllTheCreatedEventIDs
        for (Event e : repoUser.getCreatedEvents()) {
            Event createdEventFromRepo = _eventRepository.findByTitle(e.getTitle());
            createdEventFromRepo.setOwner(null);
            //eventRepository.save(createdEventFromRepo);
            repoUser.removeOwnership(e.getId());
            _userRepository.save(repoUser);
            _eventRepository.save(createdEventFromRepo);
        }


        //Remove Participation
        for (Event e : repoUser.getAttendedEvents()) {
            Event attendedEventFromRepo = _eventRepository.findByTitle(e.getTitle());
            attendedEventFromRepo.removeParticipant(repoUser.getId());
            _eventRepository.save(attendedEventFromRepo);
            repoUser.removeParticipatingEvent(e.getId());
            _userRepository.save(repoUser);
        }


        //Remove Membership
        for (Party p : repoUser.getAssociatedParties()) {
            Party partyFromRepo = _partyRepository.findByName(p.getName());
            partyFromRepo.removeMembership(repoUser.getId());
            _partyRepository.save(partyFromRepo);
            repoUser.removeAssociatedParty(partyFromRepo.getId());
            _userRepository.save(repoUser);
        }

        repoUser = _userRepository.findByCardNumber(testOwner.getCardNumber());
        _userRepository.delete(repoUser);


        assertThat(_partyRepository.count()).isEqualTo(partyCount);
        assertThat(_eventRepository.count()).isEqualTo(eventCount);
        assertThat(_userRepository.count()).isEqualTo(userCount-1);


        //Remove Participant
        repoUser = _userRepository.findByCardNumber(testParticipant.getCardNumber());

        //Remove Ownership
        //GetAllTheCreatedEventIDs
        for (Event e : repoUser.getCreatedEvents()) {
            Event createdEventFromRepo = _eventRepository.findByTitle(e.getTitle());
            createdEventFromRepo.setOwner(null);
            repoUser.removeOwnership(e.getId());
            _userRepository.save(repoUser);
            _eventRepository.save(createdEventFromRepo);
        }


        //Remove Participation
        for (Event e : repoUser.getAttendedEvents()) {
            Event attendedEventFromRepo = _eventRepository.findByTitle(e.getTitle());
            attendedEventFromRepo.removeParticipant(repoUser.getId());
            _eventRepository.save(attendedEventFromRepo);
            repoUser.removeParticipatingEvent(e.getId());
            _userRepository.save(repoUser);
        }


        //Remove Membership
        for (Party p : repoUser.getAssociatedParties()) {
            Party partyFromRepo = _partyRepository.findByName(p.getName());
            partyFromRepo.removeMembership(repoUser.getId());
            _partyRepository.save(partyFromRepo);
            repoUser.removeAssociatedParty(partyFromRepo.getId());
            _userRepository.save(repoUser);
        }

        repoUser = _userRepository.findByCardNumber(testParticipant.getCardNumber());
        _userRepository.delete(repoUser);


        //Remove a party


        Party repoParty = _partyRepository.findByName(testParty.getName());


        //RemoveMembers
        for (User u : repoParty.getMembers()) {
            u.removeAssociatedParty(repoParty.getId());
            _userRepository.save(u);
            repoParty.removeMembership(u.getId());
        }
        _partyRepository.save(repoParty);

        //RemoveEvents
        for (Event e : repoParty.getEvents()) {
            e.removeRequiredParty(repoParty.getId());
            _eventRepository.save(e);
            repoParty.removeEvent(e.getId());
        }
        _partyRepository.save(repoParty);

        _partyRepository.delete(repoParty);

        //remove an event

        assertThat(_partyRepository.count()).isEqualTo(partyCount-1);
        assertThat(_eventRepository.count()).isEqualTo(eventCount);
        assertThat(_userRepository.count()).isEqualTo(userCount-2);

    }
}