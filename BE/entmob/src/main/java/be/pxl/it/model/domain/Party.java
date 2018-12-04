package be.pxl.it.model.domain;

import be.pxl.it.model.enums.PartyType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.lang.reflect.Array;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@Entity
@Table(name = "party")
public class Party {

    //properties____________________________________

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column
    private PartyType partyType;
    @Column
    private String name;


    //nav properties_________________________________
    @ManyToMany(mappedBy = "associatedParties", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JsonIgnore
    @LazyCollection(LazyCollectionOption.FALSE)
    private Set<User> members = new HashSet<User>();

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JsonIgnore
    @JoinTable(name = "parties_events", joinColumns = @JoinColumn(name = "party_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id"))
    @LazyCollection(LazyCollectionOption.FALSE)
    private Set<Event> events = new HashSet<Event>();

    //getters setters________________________________

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Enumerated(EnumType.ORDINAL)
    public PartyType getPartyType() {
        return partyType;
    }

    public void setPartyType(PartyType partyType) {
        this.partyType = partyType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<User> getMembers() {
        return members;
    }

    public void setMembers(Set<User> members) {
        this.members = members;
    }

    public Set<Event> getEvents() {
        return events;
    }

    public void setEvents(Set<Event> events) {
        this.events = events;
    }

    public void addMember(User newMember) {
        newMember.addAssociatedParty(this);
        this.members.add(newMember);
    }

    public void addEvent(Event testEvent) {
        this.events.add(testEvent);
    }


    public void removeMembership(int memberId) {
        for (Iterator<User> userIterator = members.iterator(); userIterator.hasNext(); ) {
            User currUser = userIterator.next();
            currUser.removeAssociatedParty(getId());
            userIterator.remove();
        }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Party party = (Party) o;

        if (id != party.id) return false;
        if (partyType != party.partyType) return false;
        return name != null ? name.equals(party.name) : party.name == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (partyType != null ? partyType.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    public void removeEvent(int eventId) {
        Set<Event> newList = new HashSet<>();
        for (Event e : events) {
            if (e.getId() != eventId) {
                newList.add(e);
            }
        }
        events = newList;
    }

    public void removeAllRelations() {
        for (Event e : events) {
            e.removeRequiredParty(getId());
        }
        events.clear();
        for (User u : members) {
            u.removeAssociatedParty(getId());
        }
        members.clear();
    }
}
