package be.pxl.it.model.factories;

import be.pxl.it.dao.groep.PartyRepository;
import be.pxl.it.dao.users.UserRepository;
import be.pxl.it.model.domain.Party;
import be.pxl.it.model.domain.User;
import be.pxl.it.model.enums.PartyType;

public class PartyFactory {


    public static Party newParty(String partyName){
        Party party = new Party();
        party.setName(partyName);
        party.setPartyType(PartyType.SUPERVISORS);

        return party;
    }
}
