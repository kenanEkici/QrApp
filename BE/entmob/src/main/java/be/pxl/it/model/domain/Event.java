package be.pxl.it.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@Entity
@Table(name="event")
public class Event {

    //properties____________________________________
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column
    private String title;
    @Column
    private String description;
    @Column
    private String date;
    @Column
    private String location;
    @Column
    private String course;

    //nav properties_________________________________
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(name="event_user", joinColumns = @JoinColumn(name = "event_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
    @JsonIgnore
    private Set<User> participants = new HashSet<User>();


    @ManyToOne
    @JoinColumn (name = "user_id")
    private User owner;

    @ManyToMany(mappedBy = "events", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JsonIgnore
    @LazyCollection(LazyCollectionOption.FALSE)
    private Set<Party> requiredParties = new HashSet<Party>(); //Required Groups

    //getters setters________________________________

    public int getId() {
        return id;
    }
/*
    public void setId(int id) {
        this.id = id;
    }
*/
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public Set<User> getParticipants() {
        return participants;
    }
/*
    public void setParticipants(Set<User> participants) {
        this.participants = participants;
    }
*/
    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Set<Party> getRequiredParties() {
        return requiredParties;
    }

    /*public void setRequiredParties(Set<Party> requiredParties) {
        this.requiredParties = requiredParties;
    }
*/
    public void addRequiredParty(Party party) {
        party.addEvent(this);
        this.requiredParties.add(party);
    }

    public void addParticipant(User user){
        participants.add(user);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Event event = (Event) o;

        if (id != event.id) return false;
        if (title != null ? !title.equals(event.title) : event.title != null) return false;
        if (description != null ? !description.equals(event.description) : event.description != null) return false;
        if (date != null ? !date.equals(event.date) : event.date != null) return false;
        if (location != null ? !location.equals(event.location) : event.location != null) return false;
        return course != null ? course.equals(event.course) : event.course == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (location != null ? location.hashCode() : 0);
        result = 31 * result + (course != null ? course.hashCode() : 0);
        return result;
    }

    public void removeParticipant(int id) {
        for(Iterator<User> userIter = participants.iterator(); userIter.hasNext();){
            User currUser = userIter.next();
            if(currUser.getId() == id){
                currUser.removeParticipatingEvent(getId());
                userIter.remove();
            }
        }
    }

    public void removeRequiredParty(int id) {
        requiredParties.removeIf(currParty -> currParty.getId() == id);
    }

    public void removeOwner() {
        this.getOwner().removeOwnership(getId());
        this.setOwner(null);
    }

    public void removeParticipants() {
        for(User u : participants){
            u.removeParticipatingEvent(getId());
        }
        this.participants.clear();
    }

    public void removeRequiredParties() {
        for(Party p : requiredParties){
            p.removeEvent(getId());
        }
        requiredParties.clear();
    }

    public void removeAllRelations(){
        this.removeParticipants();
        this.removeOwner();
        this.removeRequiredParties();
    }
}
