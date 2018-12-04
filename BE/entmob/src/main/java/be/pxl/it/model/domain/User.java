package be.pxl.it.model.domain;


import be.pxl.it.model.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@Entity
@JsonIgnoreProperties
@Table(name = "user")
public class User {

    //properties____________________________________

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column
    private String role;
    @Column
    private String cardNumber;
    @Column
    private String firstName;
    @Column
    private String lastName;
    @Column
    private double pingPing;
    @Column
    private String password;
    /*
    @Column
    private String salt;
    */
    //nav properties_________________________________

    @ManyToMany(mappedBy = "participants", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JsonIgnore
    @LazyCollection(LazyCollectionOption.FALSE)
    private Set<Event> attendedEvents = new HashSet<Event>();

    @OneToMany(mappedBy = "owner", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JsonIgnore
    @LazyCollection(LazyCollectionOption.FALSE)
    private Set<Event> createdEvents = new HashSet<Event>();

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(name = "parties_users", joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "party_id"))
    @JsonIgnore
    @LazyCollection(LazyCollectionOption.FALSE)
    private Set<Party> associatedParties = new HashSet<Party>();

    //getters setters________________________________

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public double getPingPing() {
        return pingPing;
    }

    public void setPingPing(double pingPing) {
        this.pingPing = pingPing;
    }


    public int getId(){
        return this.id;
    }
    /*
    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }
    */

    public Set<Event> getAttendedEvents() {
        return attendedEvents;
    }

    public void setAttendedEvents(Set<Event> attendedEvents) {
        this.attendedEvents = attendedEvents;
    }

    public Set<Event> getCreatedEvents() {
        return createdEvents;
    }

    public void setCreatedEvents(Set<Event> createdEvents) {
        this.createdEvents = createdEvents;
    }

    public Set<Party> getAssociatedParties() {
        return associatedParties;
    }

    public void setAssociatedParty(Set<Party> associatedParties) {
        this.associatedParties = associatedParties;
    }

    public void setSpecificParty(Party party) {
        associatedParties.add(party);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void addEvent(Event e) {
        e.addParticipant(this);
        this.attendedEvents.add(e);
    }

    public void AddCreatedEvents(Event testEvent) {

        this.createdEvents.add(testEvent);
    }

    public void addAssociatedParty(Party testParty) {

        this.associatedParties.add(testParty);
    }

    public void addCreatedEvent(Event e){
        e.setOwner(this);
        this.createdEvents.add(e);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (id != user.id) return false;
        if (Double.compare(user.pingPing, pingPing) != 0) return false;
        if (role != null ? !role.equals(user.role) : user.role != null) return false;
        if (cardNumber != null ? !cardNumber.equals(user.cardNumber) : user.cardNumber != null) return false;
        if (firstName != null ? !firstName.equals(user.firstName) : user.firstName != null) return false;
        if (lastName != null ? !lastName.equals(user.lastName) : user.lastName != null) return false;
        return password != null ? password.equals(user.password) : user.password == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = id;
        result = 31 * result + (role != null ? role.hashCode() : 0);
        result = 31 * result + (cardNumber != null ? cardNumber.hashCode() : 0);
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        temp = Double.doubleToLongBits(pingPing);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (password != null ? password.hashCode() : 0);
        return result;
    }


    public void removeParticipatingEvent(int eventID) {
        attendedEvents.removeIf(currEvent -> currEvent.getId() == eventID);
    }


    public void removeOwnership(int id) {
        createdEvents.removeIf(currEvent -> currEvent.getId() == id);
    }

    private void removeAllOwnership(){
        for(Iterator<Event> eventIterator = createdEvents.iterator(); eventIterator.hasNext();){
            Event currEvent = eventIterator.next();
            currEvent.setOwner(null);
        }
        createdEvents.clear();
    }

    public void removeAssociatedParty(int id) {
        associatedParties.removeIf(currParty -> currParty.getId() == id);
    }

    private void removeAllAssociatedParties(){
        for(Iterator<Party> partyIterator = associatedParties.iterator(); partyIterator.hasNext();){
            Party currParty = partyIterator.next();
            currParty.removeMembership(getId());
        }
        associatedParties.clear();
    }

    private void removeAllParticipations(){
        for(Iterator<Event> attendedEventsIterator = attendedEvents.iterator() ; attendedEventsIterator.hasNext();){
            Event attEvent = attendedEventsIterator.next();
            attEvent.removeParticipant(getId());
        }
        attendedEvents.clear();
    }

    public void removeAllRelatonsFromUser(){
        removeAllOwnership();
        removeAllAssociatedParties();
        removeAllParticipations();
    }

}
