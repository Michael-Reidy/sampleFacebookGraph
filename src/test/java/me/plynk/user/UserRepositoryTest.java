package me.plynk.user;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedScanList;

import me.plynk.user.User;
import me.plynk.user.UserRepository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserRepositoryTest {

  @Mock
  private DynamoDBMapper dbMapper;

  @InjectMocks
  private UserRepository repository;

  @Test
  @SuppressWarnings("unchecked")
  public void readAllShouldScanTheTable() throws Exception {

    PaginatedScanList expectedResult = mock(PaginatedScanList.class);
    when(dbMapper.scan(eq(User.class), any(DynamoDBScanExpression.class))).thenReturn(expectedResult);
    List<User> result = repository.readAll();
    assertThat(result, is(expectedResult));
    verify(expectedResult).loadAllResults();
  }

  @Test
  public void readShouldReturnEmptyOptionalWhenNoResult() throws Exception {

    when(dbMapper.load(User.class, "123")).thenReturn(null);
    Optional<User> result = repository.read("123");
    assertThat(result, is(Optional.empty()));
  }

  @Test
  public void readShouldWrapResultIntoOptional() throws Exception {

    User user = new User();
    user.setFacebookKey("123");
    when(dbMapper.load(User.class, "123")).thenReturn(user);
    User result = repository.read("123").get();
    assertThat(result, is(equalTo(user)));
  }

  @Test
  public void saveShouldPersistCustomer() throws Exception {

    User user = new User();
    user.setFacebookKey("123");
    repository.save(user);
    verify(dbMapper).save(user);
  }

  @Test
  public void deleteShouldDeleteCustomerByName() throws Exception {

    repository.delete("123");
    User user = new User();
    user.setFacebookKey("123");
    verify(dbMapper).delete(eq(user), any(DynamoDBMapperConfig.class));
  }
}