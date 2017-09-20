package me.plynk.facebook;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import me.plynk.enhanceduser.EnhancedUser;
import me.plynk.user.User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class FacebookAccessService {

    public EnhancedUser enhance(User user) {
        String url = "https://graph.facebook.com/v2.10/me?fields=friends&access_token=EAACEdEose0cBABuPmjiSbpxkWPkYMD9U4EbgQqWoAcZB6b62oavxzJyV5aHdw4lhOyEH3FLMEf1TCF3G3VF5vdIiDC7ogIb83HLRjGCcCCCl4qsEIzR5JV6zeZCoNtJYZAamESlPSqqZC7C9pwstQUxxUWQFLENMjUAJZBb3ynYcH9vFJvyeISRiofcyoF6oZD";

        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(url, String.class);

        String friendCountPath = "$['friends']['summary']['total_count']";

        DocumentContext jsonContext = JsonPath.parse(response);
        Integer friendCount = jsonContext.read(friendCountPath);

        return new EnhancedUser(user, 10, "The Cure" );

    }

}
