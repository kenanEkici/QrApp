package be.pxl.it.aspect;


import be.pxl.it.jms.Producer;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Aspect
@Component
public class LoggingAspect {

    @Autowired
    private Producer producer;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    //CONTROLLER FORMAT
    //TIMESTAMP - USER - CONTROLLER - ACTION - RESULT - RESULTVALUE

    @Before("execution(* be.pxl.it.service.*.update(..))")
    public void beforeUpdate() {
        producer.SendMessage("Executing update");
    }

    @Before("execution(* be.pxl.it.service.*.add(..))")
    public void beforeAdd() {
        producer.SendMessage("Executing add on repository");
    }

    @Before("execution(* be.pxl.it.service.*.delete(..))")
    public void beforeDelete() {
        producer.SendMessage("Executing delete on repository");
    }

    //UserController
    @AfterReturning(pointcut = "execution(* be.pxl.it.controller.AuthController.login(..)) && args(userDetails)", returning = "returnValue", argNames = "userDetails,returnValue")
    public void afterLoggingIn(UserDetails userDetails, ResponseEntity returnValue) {
        String message = String.format("Time: %s - User: %s - Controller: %s - Method: %s - ReponseValue: %s", LocalDateTime.now().format(formatter), userDetails.getUsername(), "AuthController", "Get Users", returnValue.getStatusCode());
        producer.SendMessage(message);
    }

    @AfterReturning(pointcut = "execution(* be.pxl.it.controller.AuthController.getUsers(..)) && args(userDetails)", returning = "returnValue", argNames = "userDetails,returnValue")
    public void afterGetAllUsers(UserDetails userDetails, ResponseEntity returnValue) {
        String message = String.format("Time: %s - User: %s - Controller: %s - Method: %s - ReponseValue: %s", LocalDateTime.now().format(formatter), userDetails.getUsername(), "AuthController", "Get Users", returnValue.getStatusCode());
        producer.SendMessage(message);
    }

    //EventController
    @AfterReturning(pointcut = "execution(* be.pxl.it.controller.EventController.addEvent(..)) && args(userDetails)", returning = "returnValue", argNames = "userDetails,returnValue")
    public void afterAddingEvent(UserDetails userDetails, HttpStatus returnValue) {
        String message = String.format("Time: %s - User: %s - Controller: %s - Method: %s - ReponseValue: %s ", LocalDateTime.now().format(formatter), userDetails.getUsername(), "EventController", "Add Event", returnValue.value());
        producer.SendMessage(message);
    }

    @AfterReturning(pointcut = "execution(* be.pxl.it.controller.EventController.deleteEvent(..)) && args(userDetails)", returning = "returnValue", argNames = "userDetails,returnValue")
    public void afterDeleteEvent(UserDetails userDetails, HttpStatus returnValue) {
        String message = String.format("Time: %s - User: %s - Controller: %s - Method: %s - ReponseValue: %s", LocalDateTime.now().format(formatter), userDetails.getUsername(), "EventController", "Delete Event", returnValue.value());
        producer.SendMessage(message);
    }

    @AfterReturning(pointcut = "execution(* be.pxl.it.controller.EventController.updateEvent(..)) && args(userDetails)", returning = "returnValue", argNames = "userDetails,returnValue")
    public void afterUpateEvent(UserDetails userDetails, HttpStatus returnValue) {
        String message = String.format("Time: %s - User: %s - Controller: %s - Method: %s - ReponseValue: %s", LocalDateTime.now().format(formatter), userDetails.getUsername(), "EventController", "Update Event", returnValue.value());
        producer.SendMessage(message);
    }

    @AfterReturning(pointcut = "execution(* be.pxl.it.controller.EventController.getAllEvents(..)) && args(userDetails)", returning = "returnValue", argNames = "userDetails,returnValue")
    public void afterGetAllEvents(UserDetails userDetails, ResponseEntity returnValue) {
        String message = String.format("Time: %s - User: %s - Controller: %s - Method: %s - ReponseValue: %s", LocalDateTime.now().format(formatter), userDetails.getUsername(), "EventController", "Get All Events", returnValue.getStatusCode());
        producer.SendMessage(message);
    }

    @AfterReturning(pointcut = "execution(* be.pxl.it.controller.EventController.getMyEvents(..)) && args(userDetails)", returning = "returnValue", argNames = "userDetails,returnValue")
    public void afterGetMyEvents(UserDetails userDetails, ResponseEntity returnValue) {
        String message = String.format("Time: %s - User: %s - Controller: %s - Method: %s - ReponseValue: %s", LocalDateTime.now().format(formatter), userDetails.getUsername(), "EventController", "Get My Events", returnValue.getStatusCode());
        producer.SendMessage(message);
    }

    @AfterReturning(pointcut = "execution(* be.pxl.it.controller.EventController.getParticipantsOfEvent(..)) && args(userDetails)", returning = "returnValue", argNames = "userDetails,returnValue")
    public void afterGetParticipantsOfEvent(UserDetails userDetails, ResponseEntity returnValue) {
        String message = String.format("Time: %s - User: %s - Controller: %s - Method: %s - ReponseValue: %s", LocalDateTime.now().format(formatter), userDetails.getUsername(), "EventController", "Get Participants Of Event", returnValue.getStatusCode());
        producer.SendMessage(message);
    }

    @AfterReturning(pointcut = "execution(* be.pxl.it.controller.EventController.getParticipationRequiredEvents(..)) && args(userDetails)", returning = "returnValue", argNames = "userDetails,returnValue")
    public void afterGetParticipationRequiredEvents(UserDetails userDetails, ResponseEntity returnValue) {
        String message = String.format("Time: %s - User: %s - Controller: %s - Method: %s - ReponseValue: %s", LocalDateTime.now().format(formatter), userDetails.getUsername(), "EventController", "Get Participation Required Events", returnValue.getStatusCode());
        producer.SendMessage(message);

    }

    @AfterReturning(pointcut = "execution(* be.pxl.it.controller.EventController.registerParticipation(..)) && args(userDetails)", returning = "returnValue", argNames = "userDetails,returnValue")
    public void afterRegisterParticipationEvent(UserDetails userDetails, ResponseEntity returnValue) {
        String message = String.format("Time: %s - User: %s - Controller: %s - Method: %s - ReponseValue: %s", LocalDateTime.now().format(formatter), userDetails.getUsername(), "EventController", "Register Participation", returnValue.getStatusCode());
        producer.SendMessage(message);
    }


    //PartyController/*
    @AfterReturning(pointcut = "execution(* be.pxl.it.controller.PartyController.addParty(..)) && args(userDetails)", returning = "returnValue", argNames = "userDetails,returnValue")
    public void afterAddParty(UserDetails userDetails, HttpStatus returnValue) {
        String message = String.format("Time: %s - User: %s - Controller: %s - Method: %s - ReponseValue: %s", LocalDateTime.now().format(formatter), userDetails.getUsername(), "PartyController", "Update Event", returnValue.value());
        producer.SendMessage(message);
    }

    @AfterReturning(pointcut = "execution(* be.pxl.it.controller.PartyController.deleteParty(..)) && args(userDetails)", returning = "returnValue", argNames = "userDetails,returnValue")
    public void afterDeletingParty(UserDetails userDetails, HttpStatus returnValue) {
        String message = String.format("Time: %s - User: %s - Controller: %s - Method: %s - ReponseValue: %s", LocalDateTime.now().format(formatter), userDetails.getUsername(), "PartyController", "Delete Party", returnValue.value());
        producer.SendMessage(message);
    }

    @AfterReturning(pointcut = "execution(* be.pxl.it.controller.PartyController.getMembersOfParty(..)) && args(userDetails)", returning = "returnValue", argNames = "userDetails,returnValue")
    public void afterGetMembersOfParty(UserDetails userDetails, ResponseEntity returnValue) {
        String message = String.format("Time: %s - User: %s - Controller: %s - Method: %s - ReponseValue: %s", LocalDateTime.now().format(formatter), userDetails.getUsername(), "PartyController", "Get Members Of Party", returnValue.getStatusCode());
        producer.SendMessage(message);
    }

    @AfterReturning(pointcut = "execution(* be.pxl.it.controller.PartyController.getParties(..)) && args(userDetails)", returning = "returnValue", argNames = "userDetails,returnValue")
    public void afterGetAllParties(UserDetails userDetails, ResponseEntity returnValue) {
        String message = String.format("Time: %s - User: %s - Controller: %s - Method: %s - ReponseValue: %s", LocalDateTime.now().format(formatter), userDetails.getUsername(), "PartyController", "Get Parties", returnValue.getStatusCode());
        producer.SendMessage(message);
    }

}
