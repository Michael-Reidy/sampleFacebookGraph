package me.plynk.controller;

import me.plynk.user.User;
import me.plynk.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import scala.annotation.meta.setter;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/v1")
public class UserController {

  private final Logger log = LoggerFactory.getLogger(getClass());

  @Autowired
  private UserService service;

  @RequestMapping(path = "/user", method = RequestMethod.GET)
  public ResponseEntity<List<User>> list() {

    log.trace("Entering list()");
    List<User> users = service.list();
    if (users.isEmpty()) {
      return new ResponseEntity<>(NO_CONTENT);
    }
    return new ResponseEntity<>(users, OK);
  }

  @RequestMapping(path = "/user/{facebookKey}", method = RequestMethod.GET)
  public ResponseEntity<User> read(@PathVariable String facebookKey) {

    log.trace("Entering read() with {}", facebookKey);
    return service.read(facebookKey)
        .map(user -> new ResponseEntity<>(user, OK))
        .orElse(new ResponseEntity<>(NOT_FOUND));
  }

  @RequestMapping(path = "/user/sample", method = RequestMethod.GET)
  public ResponseEntity<User> sample() {

    log.trace("return a test User");
    User user = new User();
    user.setFullName("Test");
    user.setFacebookKey("9999");
    return new ResponseEntity(user, OK);
  }

  @RequestMapping(path = "/user", method = RequestMethod.POST)
  public ResponseEntity<User> create(@RequestBody @Valid User user) {

    log.trace("Entering create() with {}", user);
    return service.create(user)
        .map(newUserData -> new ResponseEntity<>(newUserData, CREATED))
        .orElse(new ResponseEntity<>(CONFLICT));
  }

  @RequestMapping(path = "/user/{facebookKey}", method = RequestMethod.PUT)
  public ResponseEntity<User> put(@PathVariable String facebookKey
          , @RequestBody User user) {

    log.trace("Entering put() with {}, {}", facebookKey, user);
    user.setFacebookKey(facebookKey);
    return service.replace(user)
        .map(newUserData -> new ResponseEntity<>(newUserData, OK))
        .orElse(new ResponseEntity<>(NOT_FOUND));
  }

  @RequestMapping(path = "/user/{facebookKey}", method = RequestMethod.PATCH)
  public ResponseEntity<User> patch(@PathVariable String facebookKey, @RequestBody User user) {

    log.trace("Entering patch() with {}, {}", facebookKey, user);
    user.setFacebookKey(facebookKey);
    return service.update(user)
        .map(newCustomerData -> new ResponseEntity<>(newCustomerData, OK))
        .orElse(new ResponseEntity<>(NOT_FOUND));
  }

  @RequestMapping(path = "/user/{facebookKey}", method = RequestMethod.DELETE)
  public ResponseEntity<Void> delete(@PathVariable String facebookKey) {

    log.trace("Entering delete() with {}", facebookKey);
    return service.delete(facebookKey) ?
        new ResponseEntity<>(NO_CONTENT) :
        new ResponseEntity<>(NOT_FOUND);
  }
}
