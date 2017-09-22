package me.plynk.facebookuser;

import lombok.Data;
import lombok.experimental.Accessors;
import me.plynk.user.User;

import java.io.Serializable;

public class FacebookUser implements Serializable {

    private int countFriends;
    private String band;
    private User user;

    public int countFriends() {
        return countFriends;
    }

    public FacebookUser countFriends(int countFriends) {
        this.countFriends = countFriends;
        return this;
    }

    public String band() {
        return band;
    }

    public FacebookUser band(String band) {
        this.band = band;
        return this;
    }

    public User user() {
        return user;
    }

    public FacebookUser user(User user) {
        this.user = user;
        return this;
    }
}
