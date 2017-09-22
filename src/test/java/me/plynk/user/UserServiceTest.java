package me.plynk.user;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import me.plynk.user.User;
import me.plynk.user.UserRepository;
import me.plynk.user.UserService;

import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.collection.IsEmptyCollection.emptyCollectionOf;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

  @Mock
  private UserRepository repository;

  @InjectMocks
  private UserService service;

  @Test
  public void readShouldReturnEmptyOptionalWhenNoUserFound() throws Exception {

    when(repository.read("l33thax0r")).thenReturn(Optional.empty());
    Optional<User> result = service.read("l33thax0r");
    assertThat(result, is(Optional.empty()));
  }

  @Test
  public void readShouldReturnResultWhenUserFound() throws Exception {

    User user = new User();
    user.setFacebookKey("l33thax0r");
    when(repository.read("l33thax0r")).thenReturn(Optional.of(user));
    User result = service.read("l33thax0r").get();
    assertThat(result, is(equalTo(user)));
  }

  @Test
  public void createShouldReturnEmptyOptionalWhenUserAlreadyExists() throws Exception {

    User existingUser = new User();
    existingUser.setFacebookKey("l33thax0r");
    existingUser.setFullName("Batman");
    when(repository.read("l33thax0r")).thenReturn(Optional.of(existingUser));
    User newUser = new User();
    newUser.setFacebookKey("l33thax0r");
    Optional<User> result = service.create(newUser);
    assertThat(result, is(Optional.empty()));
    verify(repository, never()).save(newUser);
  }

  @Test
  public void createShouldReturnnewUserWhenUserNotYetExists() throws Exception {

    User newUser = new User();
    newUser.setFacebookKey("l33thax0r");
    when(repository.read("l33thax0r")).thenReturn(Optional.empty());
    User result = service.create(newUser).get();
    assertThat(result, is(equalTo(newUser)));
    verify(repository).save(newUser);
  }

  @Test
  public void replaceShouldReturnEmptyOptionalWhenUserNotFound() throws Exception {

    User newUserData = new User();
    newUserData.setFacebookKey("l33thax0r");
    newUserData.setFullName("Batman");
    when(repository.read("l33thax0r")).thenReturn(Optional.empty());
    Optional<User> result = service.replace(newUserData);
    assertThat(result, is(Optional.empty()));
    verify(repository, never()).save(newUserData);
  }

  @Test
  public void replaceShouldOverwriteAndReturnNewDataWhenUserExists() throws Exception {

    User oldUserData = new User();
    oldUserData.setFacebookKey("l33thax0r");
    oldUserData.setFullName("Bruce Wayne");
    User newUserData = new User();
    newUserData.setFacebookKey("l33thax0r");
    newUserData.setFullName("Batman");
    when(repository.read("l33thax0r")).thenReturn(Optional.of(oldUserData));
    User result = service.replace(newUserData).get();
    assertThat(result, is(equalTo(newUserData)));
    verify(repository).save(newUserData);
  }

  @Test
  public void updateShouldReturnEmptyOptionalWhenUserNotFound() throws Exception {

    User newUserData = new User();
    newUserData.setFacebookKey("l33thax0r");
    newUserData.setFullName("Bruce Wayne");
    when(repository.read("l33thax0r")).thenReturn(Optional.empty());
    Optional<User> result = service.update(newUserData);
    assertThat(result, is(Optional.empty()));
    verify(repository, never()).save(newUserData);
  }

  @Test
  public void updateShouldOverwriteExistingFieldAndReturnNewDataWhenUserExists() throws Exception {

    User oldUserData = new User();
    oldUserData.setFacebookKey("l33thax0r");
    oldUserData.setFullName("Bruce Wayne");
    User newUserData = new User();
    newUserData.setFacebookKey("l33thax0r");
    newUserData.setFullName("Batman");
    when(repository.read("l33thax0r")).thenReturn(Optional.of(oldUserData));
    User result = service.update(newUserData).get();
    assertThat(result, is(equalTo(newUserData)));
    verify(repository).save(newUserData);
  }

  @Test
  public void updateShouldNotOverwriteExistingFieldIfNoNewValuePassedAndShouldReturnNewDataWhenUserExists() throws Exception {


    User oldUserData = new User();
    oldUserData.setFacebookKey("l33thax0r");
    User newUserData = new User();
    newUserData.setFacebookKey("l33thax0r");
    newUserData.setFullName("Bruce Wayne");
    User expectedResult = new User();
    expectedResult.setFacebookKey("l33thax0r");
    expectedResult.setFullName("Bruce Wayne");
    when(repository.read("l33thax0r")).thenReturn(Optional.of(oldUserData));
    User result = service.update(newUserData).get();
    assertThat(result, is(equalTo(expectedResult)));
    verify(repository).save(expectedResult);
  }


  @Test
  public void deleteShouldReturnFalseWhenUserNotFound() throws Exception {

    when(repository.read("l33thax0r")).thenReturn(Optional.empty());
    boolean result = service.delete("l33thax0r");
    assertThat(result, is(false));
  }

  @Test
  public void deleteShouldReturnTrueWhenUserDeleted() throws Exception {

    User user1 = new User();
    user1.setFacebookKey("l33thax0r");
    when(repository.read("l33thax0r")).thenReturn(Optional.of(user1));
    boolean result = service.delete("l33thax0r");
    assertThat(result, is(true));
    verify(repository).delete("l33thax0r");
  }

  @Test
  public void listShouldReturnEmptyListWhenNothingFound() throws Exception {

    when(repository.readAll()).thenReturn(emptyList());
    List<User> result = service.list();
    assertThat(result, is(emptyCollectionOf(User.class)));
  }

  @Test
  public void listShouldReturnAllUsers() throws Exception {

    User user1 = new User();
    user1.setFacebookKey("l33thax0r");
    User user2 = new User();
    user2.setFacebookKey("d3adb3at");
	when(repository.readAll()).thenReturn(asList(user1, user2));
    List<User> result = service.list();
    assertThat(result, containsInAnyOrder(user1, user2));
  }
}