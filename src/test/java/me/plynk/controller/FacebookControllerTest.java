package me.plynk.controller;

import me.plynk.controller.FacebookUserController;
import me.plynk.facebookuser.FacebookUser;
import me.plynk.facebookuser.FacebookUserService;
import me.plynk.user.User;
import me.plynk.user.UserService;
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

import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.*;

@RunWith(MockitoJUnitRunner.class)
public class FacebookControllerTest {

  @Mock
  private FacebookUserService facebookUserService;

  @Mock
  private UserService userService;

  @InjectMocks
  private FacebookUserController controller;

  @Test
  public void listShouldRespondWithNoContentWhenNothingInDatabase() throws Exception {

    when(userService.list()).thenReturn(emptyList());
    ResponseEntity<List<FacebookUser>> result = controller.list();
    assertThat(result, is(responseEntityWithStatus(NO_CONTENT)));
  }

  @Test
  public void listShouldRespondWithOkAndResultsFromService() throws Exception {

    User user1 = new User().facebookKey("123");
    User user2 = new User().facebookKey("999");
    FacebookUser fbuser1 = new FacebookUser().countFriends(9).band("Sisters of Mercy").user(user1);
    FacebookUser fbuser2 = new FacebookUser().countFriends(19).band("The Stone Roses").user(user2);
    when(userService.list()).thenReturn(asList(user1, user2));
    when(facebookUserService.enhance(user1)).thenReturn(fbuser1);
    when(facebookUserService.enhance(user2)).thenReturn(fbuser2);
    ResponseEntity<List<FacebookUser>> result = controller.list();
    assertThat(result, is(responseEntityThat(containsInAnyOrder(fbuser1, fbuser2))));
    assertThat(result, is(responseEntityWithStatus(OK)));
  }

  @Test
  public void readShouldReplyWithNotFoundIfNoSuchCustomer() throws Exception {

    when(userService.read("123")).thenReturn(Optional.empty());
    ResponseEntity<FacebookUser> result = controller.read("123");
    assertThat(result, is(responseEntityWithStatus(NOT_FOUND)));
  }

  @Test
  public void readShouldReplyWithCustomerIfCustomerExists() throws Exception {

    User user = new User().facebookKey("123");
    FacebookUser fbuser = new FacebookUser().countFriends(9).band("Sisters of Mercy").user(user);
    when(userService.read("123")).thenReturn(Optional.of(user));
    when(facebookUserService.enhance(user)).thenReturn(fbuser);
    ResponseEntity<FacebookUser> result = controller.read("123");
    assertThat(result, is(responseEntityWithStatus(OK)));
    assertThat(result, is(responseEntityThat(equalTo(fbuser))));
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