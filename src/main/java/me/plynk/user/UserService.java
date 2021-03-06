package me.plynk.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.amazonaws.util.StringUtils.isNullOrEmpty;

@Service
public class UserService {

  private final Logger log = LoggerFactory.getLogger(getClass());

  @Autowired
  private UserRepository repository;

  public Optional<User> read(String id) {

    log.trace("Entering read() with {}", id);
    return repository.read(id);
  }

  public Optional<User> create(User user) {

    log.trace("Entering create() with {}", user);
    if (repository.read(user.getFacebookKey()).isPresent()) {
      log.warn("User {} not found", user.getFacebookKey());
      return Optional.empty();
    }
    repository.save(user);
    return Optional.of(user);
  }

  public Optional<User> replace(User newUserData) {

    log.trace("Entering replace() with {}", newUserData);
    Optional<User> existingUser = repository.read(newUserData.getFacebookKey());
    if (!existingUser.isPresent()) {
      log.warn("User {} not found", newUserData.getFacebookKey());
      return Optional.empty();
    }
    User user = existingUser.get();
    user.setFacebookKey(newUserData.getFacebookKey());
    user.setFullName(newUserData.getFullName());

    repository.save(user);
    return Optional.of(user);
  }

  public Optional<User> update(User newUserData) {

    log.trace("Entering update() with {}", newUserData);
    Optional<User> existingUser = repository.read(newUserData.getFacebookKey());
    if (!existingUser.isPresent()) {
      log.warn("User {} not found", newUserData.getFacebookKey());
      return Optional.empty();
    }
    User user = existingUser.get();
    if (!isNullOrEmpty(newUserData.getFacebookKey())) {
      user.setFacebookKey(newUserData.getFacebookKey());
    }
    if (!isNullOrEmpty(newUserData.getFullName())) {
      user.setFullName(newUserData.getFullName());
    }
    repository.save(user);
    return Optional.of(user);
  }

  public boolean delete(String id) {

    log.trace("Entering delete() with {}", id);
    if (!repository.read(id).isPresent()) {
      log.warn("User {} not found", id);
      return false;
    }
    repository.delete(id);
    return true;
  }

  public List<User> list() {

    log.trace("Entering list()");
    return repository.readAll();
  }
}
