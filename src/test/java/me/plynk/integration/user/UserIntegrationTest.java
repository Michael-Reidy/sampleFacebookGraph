package me.plynk.integration.user;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.springframework.http.HttpStatus.OK;
import me.plynk.user.User;
import me.plynk.sample.Application;


import static java.util.Arrays.asList;
import static java.util.UUID.randomUUID;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.PATCH;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(Application.class)
@WebIntegrationTest({"server.port="})
public class UserIntegrationTest {

  @Value("${local.server.port}")
  private int port = 8080;

  private RestTemplate restTemplate = new TestRestTemplate();

  @Test
  public void postShouldCreateUserAndRespondWithCreated() throws Exception {

    User user = new User().facebookKey(randomUUID().toString());
    ResponseEntity<User> result = restTemplate.postForEntity(url("/v1/user"), user, User.class);
    assertThat(result.getStatusCode(), is(CREATED));
    assertThat(result.getBody(), is(equalTo(user)));
  }

  @Test
  public void postShouldNotCreateUserIfAlreadyExistsAndRespondWithConflict() throws Exception {

    User user = new User().facebookKey(randomUUID().toString());
    restTemplate.postForEntity(url("/v1/user"), user, User.class);
    ResponseEntity<User> result = restTemplate.postForEntity(url("/v1/user"), user, User.class);
    assertThat(result.getStatusCode(), is(CONFLICT));
  }

  @Test
  public void postShouldRespondWithBadRequestIfUserNameNotPassed() throws Exception {

    User user = new User().fullName(randomUUID().toString());
    restTemplate.postForEntity(url("/v1/user"), user, User.class);
    ResponseEntity<User> result = restTemplate.postForEntity(url("/v1/user"), user, User.class);
    assertThat(result.getStatusCode(), is(BAD_REQUEST));
  }

  @Test
  public void getShouldReturnPreviouslyCreatedUsers() throws Exception {

    User user1 = new User().facebookKey(randomUUID().toString());
    User user2 = new User().facebookKey(randomUUID().toString());
    restTemplate.postForEntity(url("/v1/user"), user1, User.class);
    restTemplate.postForEntity(url("/v1/user"), user2, User.class);
    ResponseEntity<User[]> result = restTemplate.getForEntity(url("/v1/user"), User[].class);
    assertThat(result.getStatusCode(), is(OK));
    assertThat(asList(result.getBody()), hasItems(user1, user2));
  }

  @Test
  public void getByNameShouldRespondWithNotFoundForUserThatDoesNotExist() throws Exception {

    String userName = randomUUID().toString();
    ResponseEntity<User> result = restTemplate.getForEntity(url("/v1/user/" + userName), User.class);
    assertThat(result.getStatusCode(), is(NOT_FOUND));
  }

  @Test
  public void getByNameShouldReturnPreviouslyCreatedUser() throws Exception {

    String userName = randomUUID().toString();
    User user = new User().facebookKey(userName);
    restTemplate.postForEntity(url("/v1/user"), user, User.class);
    ResponseEntity<User> result = restTemplate.getForEntity(url("/v1/user/" + userName), User.class);
    assertThat(result.getStatusCode(), is(OK));
    assertThat(result.getBody(), is(equalTo(user)));
  }

  @Test
  public void putShouldReplyWithNotFoundForUserThatDoesNotExist() throws Exception {

    String userName = randomUUID().toString();
    User user = new User().facebookKey(userName);
    RequestEntity<User> request = new RequestEntity<>(user, PUT, url("/v1/user/" + userName));
    ResponseEntity<User> result = restTemplate.exchange(request, User.class);
    assertThat(result.getStatusCode(), is(NOT_FOUND));
  }

  @Test
  public void putShouldReplaceExistingUserValues() throws Exception {

    String userName = randomUUID().toString();
    User oldUserData = new User().facebookKey(userName).fullName("1234567890");
    User newUserData = new User().facebookKey(userName).fullName("0987654321");
    restTemplate.postForEntity(url("/v1/user"), oldUserData, User.class);
    RequestEntity<User> request = new RequestEntity<>(newUserData, PUT, url("/v1/user/" + userName));
    ResponseEntity<User> result = restTemplate.exchange(request, User.class);
    assertThat(result.getStatusCode(), is(OK));
    assertThat(result.getBody(), is(equalTo(newUserData)));
  }

  @Test
  public void patchShouldReplyWithNotFoundForUserThatDoesNotExist() throws Exception {

    String userName = randomUUID().toString();
    User user = new User().facebookKey(userName);
    RequestEntity<User> request = new RequestEntity<>(user, PATCH, url("/v1/user/" + userName));
    ResponseEntity<User> result = restTemplate.exchange(request, User.class);
    assertThat(result.getStatusCode(), is(NOT_FOUND));
  }

  @Test
  public void patchShouldAddNewValuesToExistingUserValues() throws Exception {

    String userName = randomUUID().toString();
    User oldUserData = new User().facebookKey(userName);
    User newUserData = new User().facebookKey(userName).fullName("Bruce Wayne");
    User expectedNewUserData = new User().facebookKey(userName).fullName("Bruce Wayne");
    restTemplate.postForEntity(url("/v1/user"), oldUserData, User.class);
    RequestEntity<User> request = new RequestEntity<>(newUserData, PATCH, url("/v1/user/" + userName));
    ResponseEntity<User> result = restTemplate.exchange(request, User.class);
    assertThat(result.getStatusCode(), is(OK));
    assertThat(result.getBody(), is(equalTo(expectedNewUserData)));
  }

  @Test
  public void deleteShouldReturnNotFoundWhenUserDoesNotExist() throws Exception {

    String userName = randomUUID().toString();
    RequestEntity<Void> request = new RequestEntity<>(DELETE, url("/v1/user/" + userName));
    ResponseEntity<Void> result = restTemplate.exchange(request, Void.class);
    assertThat(result.getStatusCode(), is(NOT_FOUND));
  }

  @Test
  public void deleteShouldRemoveExistingUserAndRespondWithNoContent() throws Exception {

    String facebookKey = randomUUID().toString();
    User user = new User().facebookKey(facebookKey);
    restTemplate.postForEntity(url("/v1/user"), user, User.class);
    RequestEntity<Void> request = new RequestEntity<>(DELETE, url("/v1/user/" + facebookKey));
    ResponseEntity<Void> result = restTemplate.exchange(request, Void.class);
    assertThat(result.getStatusCode(), is(NO_CONTENT));
  }

  private URI url(String url) {

    return URI.create("http://localhost:" + port + url);
  }
}
