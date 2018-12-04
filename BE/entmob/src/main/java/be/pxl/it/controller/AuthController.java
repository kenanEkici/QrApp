package be.pxl.it.controller;

import be.pxl.it.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/authorize")
public class AuthController {

    private final UserService _userService;

    @Autowired
    public AuthController(UserService userService) {
        _userService = userService;
    }

    //check passed credentials and verify user
    @GetMapping
    public ResponseEntity login(@AuthenticationPrincipal final UserDetails user) {
        return new ResponseEntity<>(_userService.getByCardNumber(user.getUsername()), HttpStatus.OK);
    }

    @GetMapping("/users")
    public ResponseEntity getUsers(@AuthenticationPrincipal final UserDetails user) {
        return new ResponseEntity<>(_userService.getAllUsers(), HttpStatus.OK);
    }
}
