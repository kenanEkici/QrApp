package be.pxl.it.dao.groep;

import be.pxl.it.model.domain.Party;
import org.springframework.data.repository.CrudRepository;


public interface PartyRepository extends CrudRepository<Party, Integer> {
    Party findByName(String name);
}
