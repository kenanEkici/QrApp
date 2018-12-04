package be.pxl.it.entmob.normal;

import be.pxl.it.EntmobApplication;
import be.pxl.it.dao.events.EventRepository;
import be.pxl.it.dao.groep.PartyRepository;
import be.pxl.it.dao.users.UserRepository;
import be.pxl.it.entmob.TestBuilder;
import be.pxl.it.model.domain.Event;
import be.pxl.it.model.domain.Party;
import be.pxl.it.model.domain.User;
import be.pxl.it.service.EventService;
import be.pxl.it.service.PartyService;
import be.pxl.it.service.UserService;
import org.hamcrest.core.IsEqual;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.setRemoveAssertJRelatedElementsFromStackTrace;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.times;


@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = EntmobApplication.class)
public class ServiceTest {
    @Autowired
    private EventService _eventService;
    @Autowired
    private UserService _userService;
    @Autowired
    private PartyService _partyService;

    @Mock
    UserRepository _userRepository;

    @Mock
    PartyRepository _partyRepository;

    @Mock
    EventRepository _eventRepository;


    @Before
    public void setUp() throws Exception {
        _userService = new UserService(_userRepository);
        _eventService = new EventService(_eventRepository, _userRepository, _partyRepository);
        _partyService = new PartyService(_partyRepository, _userRepository, _eventRepository);

    }

    //UserService

    @Test
    public void getAllUsersFromRepository_ReturnsAllUsersFromDatabase() throws Exception {
        User testUser = TestBuilder.user();
        User testUser2 = TestBuilder.user();

        testUser2.setCardNumber(UUID.randomUUID().toString());
        ArrayList<User> userIterableFromRepo = new ArrayList<User>();

        userIterableFromRepo.add(testUser);
        userIterableFromRepo.add(testUser2);

        Mockito.when(_userRepository.findAll()).thenReturn(userIterableFromRepo);
        Iterable<User> serviceUserList = _userService.getAllUsers();

        for (User i : serviceUserList) {
            if (i.getCardNumber().equals(testUser.getCardNumber()))
                assertThat(testUser).isEqualTo(i);
            if (i.getCardNumber().equals(testUser2.getCardNumber()))
                assertThat(testUser2).isEqualTo(i);
        }
    }

    @Test
    public void getSpecifiedUserFromRepository_ReturnsCorrectUser() throws Exception {
        User testUser = TestBuilder.user();

        _userRepository.save(testUser);

        Mockito.when(_userRepository.findByCardNumber(testUser.getCardNumber())).thenReturn(testUser);

        User userFromRepo = _userService.getByCardNumber(testUser.getCardNumber());

        assertThat(userFromRepo).isEqualTo(testUser);
    }

    @Test
    public void addUserToRepository_VerifyIfUserIsUsed() throws Exception {
        User testUser = TestBuilder.user();
        _userService.add(testUser);
        Mockito.verify(_userRepository, times(1)).save(testUser);
    }

    @Test
    public void deleteUserFromRepository_VerifyIfMethodIsUsed() throws Exception {

        User testUser = TestBuilder.user();
        Mockito.when(_userRepository.findOne(anyInt())).thenReturn(testUser);

        _userService.delete(testUser);


        Mockito.verify(_userRepository, times(1)).findOne(anyInt());
        Mockito.verify(_userRepository, times(1)).save(any(User.class));
        Mockito.verify(_userRepository, times(1)).delete(any(User.class));
    }

    @Test
    public void updateUserFromRepository_VerifyIfMethodIsUsed() throws Exception {
        User testUser = TestBuilder.user();

        _userService.update(testUser);
        Mockito.verify(_userRepository, times(1)).save(testUser);
    }


    //EventService

    @Test
    public void getAllEvent_ReturnsAllEvents() throws Exception {

        Event testEvent = TestBuilder.event();
        Event testEvent2 = TestBuilder.event();
        ArrayList<Event> eventList = new ArrayList<>();
        eventList.add(testEvent);
        eventList.add(testEvent2);

        Mockito.when(_eventRepository.findAll()).thenReturn(eventList);
        Iterable<Event> repoEvents = _eventService.getAllEvents();

        for (Event i : repoEvents) {
            if (i.getTitle().equals(testEvent.getTitle()))
                assertThat(testEvent).isEqualTo(i);
            if (i.getTitle().equals(testEvent2.getTitle()))
                assertThat(testEvent2).isEqualTo(i);
        }
    }

    @Test
    public void findParticipantsOfEvent_ReturnsParticipantsOfEvent() throws Exception {

        User testUser = TestBuilder.user();
        User testUser2 = TestBuilder.user();

        Event testEvent = TestBuilder.event();
        testUser.addEvent(testEvent);
        testUser2.addEvent(testEvent);

        Mockito.when(_userRepository.findByCardNumber(testUser.getCardNumber())).thenReturn(testUser);
        Mockito.when(_eventRepository.findOne(testEvent.getId())).thenReturn(testEvent);

        Iterable<User> participantList = _eventService.findParticipantsOfEvent(testEvent.getId());

        for (User i : participantList) {
            if (i.getCardNumber().equals(testUser.getCardNumber()))
                assertThat(testUser).isEqualTo(i);
            if (i.getCardNumber().equals(testUser2.getCardNumber()))
                assertThat(testUser2).isEqualTo(i);
        }
    }


    @Test
    public void scanParticipant_ReturnsAllParticipantsOfEvent() throws Exception {
        User testUser = TestBuilder.user();
        Event testEvent = TestBuilder.event();

        Mockito.when(_eventRepository.findOne(anyInt())).thenReturn(testEvent);
        Mockito.when(_userRepository.findByCardNumber(testUser.getCardNumber())).thenReturn(testUser);

        Iterable<User> participantList = _eventService.scanParticipant(1, testUser.getCardNumber());

        assertThat(((Collection<?>) participantList).size()).isEqualTo(1);
    }

    @Test
    public void registerParticipation_UserExist_ReturnsTrue() throws Exception {
        User testUser = TestBuilder.user();
        Event testEvent = TestBuilder.event();

        Mockito.when(_eventRepository.findOne(anyInt())).thenReturn(testEvent);
        Mockito.when(_userRepository.findByCardNumber(testUser.getCardNumber())).thenReturn(testUser);

        boolean registeredBool = _eventService.registerParticipation(testEvent.getId(), testUser.getCardNumber());

        assertThat(registeredBool).isEqualTo(true);
    }

    @Test
    public void registerParticipation_UserDoesNotExist_ReturnsFalse() throws Exception {
        Event testEvent = TestBuilder.event();

        Mockito.when(_eventRepository.findOne(anyInt())).thenReturn(testEvent);

        boolean registerBool = _eventService.registerParticipation(1, "2");

        assertThat(registerBool).isEqualTo(false);
    }

    @Test
    public void findParticipationRequiredEvents_ReturnsEventsWhereUserNeedsToParticipate() throws Exception {
        Event testEvent = TestBuilder.event();
        User testUser = TestBuilder.user();
        Party testParty = TestBuilder.party();

        testEvent.addRequiredParty(testParty);
        testParty.addMember(testUser);


        Mockito.when(_userRepository.findByCardNumber(testUser.getCardNumber())).thenReturn(testUser);

        Iterable<Event> RequiredParticipationList = _eventService.findParticipationRequiredEvents(testUser.getCardNumber());

        assertThat(((Collection<?>)RequiredParticipationList).size()).isEqualTo(1);

    }

    @Test
    public void getAllEvents_ReturnsAllEvents() throws Exception {

        ArrayList<Event> eventArrayList = new ArrayList<>();
        eventArrayList.add(TestBuilder.event());
        Event testEvent = TestBuilder.event();
        testEvent.setTitle("TweedeEvent");
        eventArrayList.add(testEvent);

        Mockito.when(_eventRepository.findAll()).thenReturn(eventArrayList);

        Iterable<Event> listFromService = _eventService.getAllEvents();

        assertThat(((Collection<?>) listFromService).size()).isEqualTo(2);
    }

    @Test
    public void getMyEvents_ReturnsEventsCreatedByUser() throws Exception {

        User testUser = TestBuilder.user();
        Event testEvent = TestBuilder.event();
        Event testEvent2 = TestBuilder.event();
        testEvent.setTitle("TestEvent2");

        testUser.addCreatedEvent(testEvent);
        testUser.addCreatedEvent(testEvent2);

        ArrayList<Event> eventList = new ArrayList<>();
        eventList.add(testEvent);
        eventList.add(testEvent2);

        Mockito.when(_eventRepository.findAll()).thenReturn(eventList);

        Iterable<Event> createdEvents = _eventService.getMyEvents(testUser.getCardNumber());

        assertThat(((Collection<?>) createdEvents).size()).isEqualTo(2);
    }


    //PartyService

    @Test
    public void getAllMembers_ReturnsAllMembersOfGivenParty() throws Exception {
        Party testParty = TestBuilder.party();

        User testUser = TestBuilder.user();
        User testUser2 = TestBuilder.user();

        testParty.addMember(testUser);
        testParty.addMember(testUser2);

        Mockito.when(_partyRepository.findOne(anyInt())).thenReturn(testParty);

        Collection<User> membersOfParty = (Collection<User>) _partyService.getAllMembers(2);

        assertThat(membersOfParty.size()).isEqualTo(2);
    }
}


