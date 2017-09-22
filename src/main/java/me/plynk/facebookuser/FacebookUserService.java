package me.plynk.facebookuser;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import me.plynk.facebookuser.FacebookUser;
import me.plynk.user.User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class FacebookUserService {

    private static final String FACEBOOK_USER_URL_TEMPLATE = "https://graph.facebook.com/v2.10/me?fields=id,name,about,music,friends&access_token=%s";
    private static final String TOTAL_FRIEND_COUNT_QUERY =  "$.friends.summary.total_count";
    private static final String MUSIC_DATA_FIRST_QUERY =  "$.music.data[0].name";

    public FacebookUser enhance(User user) {
        String url = String.format(FACEBOOK_USER_URL_TEMPLATE,user.getFacebookKey());

        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(url, String.class);

        DocumentContext jsonContext = JsonPath.parse(response);
        Integer friendCount = jsonContext.read(TOTAL_FRIEND_COUNT_QUERY);
        String band = jsonContext.read(MUSIC_DATA_FIRST_QUERY);

        FacebookUser facebookUser = new FacebookUser();
        facebookUser.setUser(user);
        facebookUser.setCountFriends(friendCount);
        facebookUser.setBand(band);
        return facebookUser;
    }

}
