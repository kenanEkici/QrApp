package be.pxl.it.model.factories;

import be.pxl.it.model.domain.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.UUID;

public class UserFactory {

    public static User newStudent(String firstName, String lastName){
        User user = new User();
        user.setCardNumber(firstName+lastName);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPassword(new BCryptPasswordEncoder().encode("password"));
        user.setPingPing(5.00F);
        user.setRole("ROLE_STUDENT");

        return user;
    }

    public static User newTeacher(String firstName, String lastName){
        User user = new User();
        user.setCardNumber(firstName+lastName);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPassword(new BCryptPasswordEncoder().encode("password"));
        user.setPingPing(5.00F);
        user.setRole("ROLE_TEACHER");

        return user;
    }
}

