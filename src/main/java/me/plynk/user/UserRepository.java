package me.plynk.user;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig.SaveBehavior;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository {

  private final Logger log = LoggerFactory.getLogger(getClass());

  @Autowired
  private DynamoDBMapper dbMapper;

  public List<User> readAll() {

    log.trace("Entering readAll()");
    PaginatedList<User> results = dbMapper.scan(User.class, new DynamoDBScanExpression());
    results.loadAllResults();
    return results;
  }

  public Optional<User> read(String facebookKey) {

    log.trace("Entering read() with {}", facebookKey);
    return Optional.ofNullable(dbMapper.load(User.class, facebookKey));
  }

  public void save(User user) {

    log.trace("Entering save() with {}", user);
    dbMapper.save(user);
  }

  public void delete(String facebookKey) {

    dbMapper.delete(new User().facebookKey(facebookKey), new DynamoDBMapperConfig(SaveBehavior.CLOBBER));
  }
}
