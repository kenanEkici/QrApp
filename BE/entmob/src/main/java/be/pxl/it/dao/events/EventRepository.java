package be.pxl.it.dao.events;

import be.pxl.it.model.domain.Event;
import org.springframework.data.repository.CrudRepository;

public interface EventRepository extends CrudRepository<Event, Integer> {
    Event findByTitle(String title);
}
