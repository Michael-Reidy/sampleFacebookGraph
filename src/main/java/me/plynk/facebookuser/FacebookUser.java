package me.plynk.facebookuser;

import lombok.Data;
import lombok.experimental.Accessors;
import me.plynk.user.User;

import java.io.Serializable;

public class FacebookUser implements Serializable {

    private int countFriends;
    private String band;
    private User user;

    public int getCountFriends() {
        return countFriends;
    }

    public void setCountFriends(int countFriends) {
        this.countFriends = countFriends;
    }

    public String getBand() {
        return band;
    }

    public void setBand(String band) {
        this.band = band;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FacebookUser)) return false;

        FacebookUser that = (FacebookUser) o;

        if (countFriends != that.countFriends) return false;
        if (band != null ? !band.equals(that.band) : that.band != null) return false;
        return user != null ? user.equals(that.user) : that.user == null;
    }

    @Override
    public int hashCode() {
        int result = countFriends;
        result = 31 * result + (band != null ? band.hashCode() : 0);
        result = 31 * result + (user != null ? user.hashCode() : 0);
        return result;
    }
}
