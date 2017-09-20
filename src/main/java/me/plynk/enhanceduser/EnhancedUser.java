package me.plynk.enhanceduser;

import me.plynk.user.User;

public class EnhancedUser {
    private int countFriends;
    private String band;
    private User user;

    public EnhancedUser(User user, int countFriends, String band) {
        this.user = user;
        this.countFriends = countFriends;
        this.band = band;
    }
}
