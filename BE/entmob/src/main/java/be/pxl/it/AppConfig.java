package be.pxl.it;

import be.pxl.it.dao.events.EventRepository;
import be.pxl.it.dao.groep.PartyRepository;
import be.pxl.it.dao.users.UserRepository;
import be.pxl.it.model.domain.Event;
import be.pxl.it.model.domain.Party;
import be.pxl.it.model.domain.User;
import be.pxl.it.model.enums.Role;
import be.pxl.it.model.factories.EventFactory;
import be.pxl.it.model.factories.PartyFactory;
import be.pxl.it.model.factories.UserFactory;
import be.pxl.it.service.UserService;
import org.hibernate.annotations.FetchProfiles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Profile;

import java.util.*;

@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
@Profile("production")
public class AppConfig {

    @Bean
    @Autowired
    public CommandLineRunner demo(EventRepository eventRepository, UserRepository userRepository, PartyRepository partyRepository) {
        return (args) -> {

/*
            Event event = EventFactory.newEvent();
            User teacher = UserFactory.newTeacher();
            User student = UserFactory.newStudent();
            Party party = PartyFactory.newParty();
            Event eventTwo = EventFactory.newEvent();
            eventTwo.addParticipant(student);
            eventTwo.setOwner(teacher);


            //eventRepository.save(event);
            userRepository.save(teacher);
            //userRepository.save(student);
            //partyRepository.save(party);

            student.setSpecificParty(party);
            event.addParticipant(student);
            //userRepository.save(student); //update

            event.setOwner(teacher);
            //eventRepository.save(event); //update

            HashSet<Event> events = new HashSet<>();
            events.add(event);
            events.add(eventTwo);
            party.setEvents(events);

            //partyRepository.save(party); //update
            eventRepository.save(event);


            //event.addParticipant(student);
            //userRepository.save(student); //update
*/

            //Best Seed Ever


            //Users

            User studentIsabelle = UserFactory.newStudent("Anaballe", "IkBenNietsZonderJou");
            User studentSimon = UserFactory.newStudent("Simon", "Bogaerts");
            User studentKenan = UserFactory.newStudent("Kenan", "Ekici");
            User studentSinasi = UserFactory.newStudent("Sinasi", "Yilmaz");
            User studentPieterJan = UserFactory.newStudent("Pieter-Jan", "Kerfs");


            User teacherWesley = UserFactory.newTeacher("Wesley", "Hendrix");
            User teacherKris = UserFactory.newTeacher("Kris", "Hermans");
            User teacherMarijke = UserFactory.newTeacher("Marijke", "Willems");
            User teacherTom = UserFactory.newTeacher("Tom", "Schuyten");
            User teacherFrans = UserFactory.newTeacher("Frans", "Guelinx");

            teacherFrans.setCardNumber("02");
            User supervisorBigBrother = UserFactory.newStudent("BigBigBrother", "I am the one who rules the galaxy");
            supervisorBigBrother.setRole("ROLE_SUPERVISOR");

            //Events

            Event eventProgExp = EventFactory.newEvent("Programming Expert");
            Event eventEntMob = EventFactory.newEvent("Enterprise and mobile");
            Event eventMobDev = EventFactory.newEvent("Mobile Development");
            Event eventWebMob = EventFactory.newEvent("Web and mobile");
            Event eventHandshake = EventFactory.newEvent("Handshake Event");

            //UserGroups

            Party partyAONA = PartyFactory.newParty("AONA");
            Party partyAONB = PartyFactory.newParty("AONB");
            Party partySWM = PartyFactory.newParty("SWM");
            Party partySNB = PartyFactory.newParty("SNB");
            Party partyTeachersClub = PartyFactory.newParty("TeachersClub");


            //Link userGroups

            partyAONA.addMember(studentSimon);
            partyAONB.addMember(studentKenan);
            partyAONB.addMember(studentPieterJan);
            partySNB.addMember(studentSinasi);
            partySWM.addMember(studentIsabelle);
            partyTeachersClub.addMember(teacherFrans);
            partyTeachersClub.addMember(teacherKris);
            partyTeachersClub.addMember(teacherMarijke);
            partyTeachersClub.addMember(teacherTom);
            partyTeachersClub.addMember(teacherWesley);





            //LinkOwners
            teacherKris.addCreatedEvent(eventProgExp);
            teacherMarijke.addCreatedEvent(eventHandshake);
            teacherTom.addCreatedEvent(eventEntMob);
            teacherWesley.addCreatedEvent(eventWebMob);
            teacherFrans.addCreatedEvent(eventMobDev);





            //LinkRequiredParties

            eventEntMob.addRequiredParty(partyAONA);
            eventEntMob.addRequiredParty(partyAONB);
            eventMobDev.addRequiredParty(partySNB);
            eventProgExp.addRequiredParty(partySNB);
            eventProgExp.addRequiredParty(partySWM);
            eventWebMob.addRequiredParty(partyTeachersClub);

            eventHandshake.addRequiredParty(partyAONA);
            eventHandshake.addRequiredParty(partyAONB);
            eventHandshake.addRequiredParty(partyAONA);
            eventHandshake.addRequiredParty(partySNB);
            eventHandshake.addRequiredParty(partySWM);
            eventHandshake.addRequiredParty(partyTeachersClub);



            //AddParticipation?


            studentPieterJan.addEvent(eventEntMob);
            studentSimon.addEvent(eventEntMob);
            studentSinasi.addEvent(eventEntMob);
            studentKenan.addEvent(eventEntMob);

            partyRepository.save(partyAONB);
            partyRepository.save(partySWM);
            partyRepository.save(partySNB);
            partyRepository.save(partyTeachersClub);

            userRepository.save(teacherFrans);
            userRepository.save(teacherKris);
            userRepository.save(teacherMarijke);
            userRepository.save(teacherTom);
            userRepository.save(studentIsabelle);
            userRepository.save(studentKenan);
            userRepository.save(studentPieterJan);
            userRepository.save(studentSimon);
            userRepository.save(studentSinasi);
            userRepository.save(supervisorBigBrother);

        };
    }
}

