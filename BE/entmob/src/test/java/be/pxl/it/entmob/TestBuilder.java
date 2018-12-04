package be.pxl.it.entmob;

import be.pxl.it.model.domain.Event;
import be.pxl.it.model.domain.Party;
import be.pxl.it.model.domain.User;
import be.pxl.it.model.enums.PartyType;
import be.pxl.it.model.enums.Role;

import java.util.UUID;

public class TestBuilder {
    public static Event event() {
        Event event = new Event();
        event.setTitle("test_title 1");
        event.setCourse("java 1");
        event.setDate("today 1");
        event.setDescription("description 1");
        event.setLocation("locatie 1");
        return event;
    }

    public static Party party() {
        Party party = new Party();
        party.setName("testName");
        party.setPartyType(PartyType.SUPERVISORS);
        return party;
    }

    public static User user(){
        User user = new User();
        user.setCardNumber(UUID.randomUUID().toString());
        user.setFirstName("first_name");
        user.setLastName("last_name");
        user.setPingPing(5.00F);
        user.setRole("SUPERVISOR");
        return user;
    }

}
