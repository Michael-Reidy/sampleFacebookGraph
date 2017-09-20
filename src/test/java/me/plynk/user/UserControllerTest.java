package me.plynk.user;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import me.plynk.user.User;
import me.plynk.user.UserController;
import me.plynk.user.UserService;

import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {

  @Mock
  private UserService service;

  @InjectMocks
  private UserController controller;

  @Test
  public void listShouldRespondWithNoContentWhenNothingInDatabase() throws Exception {

    when(service.list()).thenReturn(emptyList());
    ResponseEntity<List<User>> result = controller.list();
    assertThat(result, is(responseEntityWithStatus(NO_CONTENT)));
  }

  @Test
  public void listShouldRespondWithOkAndResultsFromService() throws Exception {

    User user1 = new User().facebookKey("123");
    User user2 = new User().facebookKey("999");
    when(service.list()).thenReturn(asList(user1, user2));
    ResponseEntity<List<User>> result = controller.list();
    assertThat(result, is(responseEntityThat(containsInAnyOrder(user1, user2))));
    assertThat(result, is(responseEntityWithStatus(OK)));
  }

  @Test
  public void readShouldReplyWithNotFoundIfNoSuchCustomer() throws Exception {

    when(service.read("123")).thenReturn(Optional.empty());
    ResponseEntity<User> result = controller.read("123");
    assertThat(result, is(responseEntityWithStatus(NOT_FOUND)));
  }

  @Test
  public void readShouldReplyWithCustomerIfCustomerExists() throws Exception {

    User customer = new User().facebookKey("123");
    when(service.read("123")).thenReturn(Optional.of(customer));
    ResponseEntity<User> result = controller.read("123");
    assertThat(result, is(responseEntityWithStatus(OK)));
    assertThat(result, is(responseEntityThat(equalTo(customer))));
  }

  @Test
  public void createShouldReplyWithConflictIfCustomerAlreadyExists() throws Exception {

    User customer = new User().facebookKey("123");
    when(service.create(customer)).thenReturn(Optional.empty());
    ResponseEntity<User> result = controller.create(customer);
    assertThat(result, is(responseEntityWithStatus(CONFLICT)));
  }

  @Test
  public void createShouldReplyWithCreatedAndCustomerData() throws Exception {

    User user = new User().facebookKey("123");
    when(service.create(user)).thenReturn(Optional.of(user));
    ResponseEntity<User> result = controller.create(user);
    assertThat(result, is(responseEntityWithStatus(CREATED)));
    assertThat(result, is(responseEntityThat(equalTo(user))));
  }

  @Test
  public void putShouldReplyWithNotFoundIfCustomerDoesNotExist() throws Exception {

    User newCustomerData = new User().facebookKey("123");
    when(service.replace(newCustomerData)).thenReturn(Optional.empty());
    ResponseEntity<User> result = controller.put("123", new User());
    assertThat(result, is(responseEntityWithStatus(NOT_FOUND)));
  }

  @Test
  public void putShouldReplyWithUpdatedCustomerAndOkIfCustomerExists() throws Exception {

    User newCustomerData = new User().facebookKey("123");
    when(service.replace(newCustomerData)).thenReturn(Optional.of(newCustomerData));
    ResponseEntity<User> result = controller.put("123", new User());
    assertThat(result, is(responseEntityWithStatus(OK)));
    assertThat(result, is(responseEntityThat(equalTo(newCustomerData))));
  }

  @Test
  public void patchShouldReplyWithNotFoundIfCustomerDoesNotExist() throws Exception {

    User newCustomerData = new User().facebookKey("123");
    when(service.update(newCustomerData)).thenReturn(Optional.empty());
    ResponseEntity<User> result = controller.patch("123", new User());
    assertThat(result, is(responseEntityWithStatus(NOT_FOUND)));
  }

  @Test
  public void patchShouldReplyWithUpdatedCustomerAndOkIfCustomerExists() throws Exception {

    User newCustomerData = new User().facebookKey("123");
    when(service.update(newCustomerData)).thenReturn(Optional.of(newCustomerData));
    ResponseEntity<User> result = controller.patch("123", new User());
    assertThat(result, is(responseEntityWithStatus(OK)));
    assertThat(result, is(responseEntityThat(equalTo(newCustomerData))));
  }

  @Test
  public void deleteShouldRespondWithNotFoundIfCustomerDoesNotExist() throws Exception {

    when(service.delete("123")).thenReturn(false);
    ResponseEntity<Void> result = controller.delete("123");
    assertThat(result, is(responseEntityWithStatus(NOT_FOUND)));
  }

  @Test
  public void deleteShouldRespondWithNoContentIfDeleteSuccessful() throws Exception {

    when(service.delete("123")).thenReturn(true);
    ResponseEntity<Void> result = controller.delete("123");
    assertThat(result, is(responseEntityWithStatus(NO_CONTENT)));
  }

  private Matcher<ResponseEntity> responseEntityWithStatus(HttpStatus status) {

    return new TypeSafeMatcher<ResponseEntity>() {

      @Override
      protected boolean matchesSafely(ResponseEntity item) {

        return status.equals(item.getStatusCode());
      }

      @Override
      public void describeTo(Description description) {

        description.appendText("ResponseEntity with status ").appendValue(status);
      }
    };
  }

  private <T> Matcher<ResponseEntity<? extends T>> responseEntityThat(Matcher<T> categoryMatcher) {

    return new TypeSafeMatcher<ResponseEntity<? extends T>>() {
      @Override
      protected boolean matchesSafely(ResponseEntity<? extends T> item) {

        return categoryMatcher.matches(item.getBody());
      }

      @Override
      public void describeTo(Description description) {

        description.appendText("ResponseEntity with ").appendValue(categoryMatcher);
      }
    };
  }
}