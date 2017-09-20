package me.plynk.enchanceduser;

import me.plynk.enhanceduser.EnhancedUser;
import me.plynk.facebook.FacebookAccessService;
import me.plynk.user.User;
import me.plynk.user.UserController;
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
import static org.mockito.Matchers.isNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.*;
import org.springframework.beans.factory.annotation.Autowired;

@RunWith(MockitoJUnitRunner.class)
public class EnhancedUserTest {

  @InjectMocks
  private FacebookAccessService facebookAccessService;

  @Test
  public void foo() throws Exception {
    EnhancedUser user = facebookAccessService.enhance(new User());
  }

}