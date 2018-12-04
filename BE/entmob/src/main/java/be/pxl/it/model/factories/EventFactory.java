package be.pxl.it.model.factories;

import be.pxl.it.dao.events.EventRepository;
import be.pxl.it.dao.users.UserRepository;
import be.pxl.it.model.domain.Event;
import be.pxl.it.model.domain.User;
import org.omg.PortableInterceptor.USER_EXCEPTION;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

public class EventFactory
{

    public static Event newEvent(String title){
        Event event = new Event();
        event.setTitle(title);
        event.setCourse("java");
        event.setDate(LocalDateTime.now().toString());
        event.setDescription("description ");
        event.setLocation("locatie");
        return event;
    }
}
