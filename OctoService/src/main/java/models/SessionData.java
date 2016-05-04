package models;

/**
 * Created by Alexander on 21/03/16.
 */
public class SessionData {
    private long userId;
    private String token;

    public SessionData() {}

    public SessionData(long userId, String token) {
        this.userId = userId;
        this.token = token;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
