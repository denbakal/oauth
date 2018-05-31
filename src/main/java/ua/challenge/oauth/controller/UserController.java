package ua.challenge.oauth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ua.challenge.oauth.config.oauth2.principal.TokenData;
import ua.challenge.oauth.model.User;

import java.security.Principal;
import java.util.Collections;
import java.util.List;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;

@RestController
public class UserController {
//    @PreAuthorize("#oauth2.hasScope('read')")
    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUser(@PathVariable("id") long id) {
        User currentUser = new User(Long.parseLong(randomNumeric(2)), randomAlphabetic(4));
        return new ResponseEntity<>(currentUser, HttpStatus.OK);
    }

//    @PreAuthorize("#oauth2.hasScope('read')")
    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers(Principal principal, OAuth2Authentication auth) {
        System.out.println("principal: " + principal);
        System.out.println("name of principal: " + principal);
        System.out.println("token: " + auth);
        System.out.println("token: " + auth.getOAuth2Request());

        TokenData tokenData = (TokenData) auth.getPrincipal();

        System.out.println("tokenData = " + tokenData.getEmail());

        return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);
    }
}
