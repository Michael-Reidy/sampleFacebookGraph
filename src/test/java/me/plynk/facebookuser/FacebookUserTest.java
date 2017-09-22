package me.plynk.facebookuser;

import me.plynk.user.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.Serializable;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.isNotNull;

@RunWith(MockitoJUnitRunner.class)
public class FacebookUserTest implements Serializable {

  @InjectMocks
  private FacebookUserService facebookAccessService;

  private static final String FACEBOOK_ACCESS_TOKEN = "EAACEdEose0cBAKUOfEJAM2NRsUoMWCE71YXZBD3AV8vBE3IvPyZAyW5LShfqFnKYn9L1whTxL2yaBlc1PFVJwfUZAHwnThgISbp9EjuVXCJWT5i1umkTIx80lcL9rVPIxTBBskNMk4NLjLpXMU0xBQSX3ezDBaxUdt1FZBPY7LwB3KlPu1ZBh2k0gcN13WZCTXGoJdXlyy0ZCCZBXyH9IvbMneoD5LlR6nMDZBGKJ9h1JrQZDZD";

  @Test
  public void foo() throws Exception {
    //FacebookUser user = facebookAccessService.enhance(new User().facebookKey(FACEBOOK_ACCESS_TOKEN).fullName("Me"));
  }

}