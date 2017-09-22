package me.plynk.controller;

import me.plynk.facebookuser.FacebookUser;
import me.plynk.facebookuser.FacebookUserService;
import me.plynk.user.User;
import me.plynk.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/v1")
public class FacebookUserController {

  private final Logger log = LoggerFactory.getLogger(getClass());

  @Autowired
  private FacebookUserService facebookUserService;
  
  @Autowired
  private UserService userService;

  @RequestMapping(path = "/facebookuser/sample", method = RequestMethod.GET)
  public ResponseEntity<FacebookUser> sample() {

    log.trace("return a test User");
    User user = new User();
    user.setFullName("Test");
    user.setFacebookKey("9999");

    FacebookUser facebookUser = new FacebookUser();
    facebookUser.setCountFriends(9999);
    facebookUser.setBand("Band");
    facebookUser.setUser(user);
    return new ResponseEntity(facebookUser, OK);
  }

  @RequestMapping(path = "/facebookuser", method = RequestMethod.GET)
  public ResponseEntity<List<FacebookUser>> list() {

    log.trace("Entering list()");
    List<User> users = userService.list();
    List<FacebookUser> facebookUsers = users.stream().map(user -> facebookUserService.enhance(user)).collect(Collectors.toList());
    if (facebookUsers.isEmpty()) {
      return new ResponseEntity<>(NO_CONTENT);
    }
    return new ResponseEntity<>(facebookUsers, OK);
  }

  @RequestMapping(path = "/facebookuser/{facebookKey}", method = RequestMethod.GET)
  public ResponseEntity<FacebookUser> read(@PathVariable String facebookKey) {

    log.trace("Entering read() with {}", facebookKey);
    return userService.read(facebookKey).map(user -> facebookUserService.enhance(user))
        .map(facebookuser -> new ResponseEntity<>(facebookuser, OK))
        .orElse(new ResponseEntity<>(NOT_FOUND));
  }
}
