package models;

import javax.json.Json;
import javax.json.JsonObject;

/**
 * Created by Alexander on 20/03/16.
 */
public class UserShort {
    private long id;
    private String login;
    private String password;
    private String name;
    private String avatarFile;

    private long followersCount;
    private long subscriptionsCount;

    private long meshesCount;
    private long likesCount;

    public UserShort() {}

    public UserShort(long id, String login, String password, String name, String avatarFile) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.name = name;
        this.avatarFile = avatarFile;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatarFile() {
        return avatarFile;
    }

    public void setAvatarFile(String avatarFile) {
        this.avatarFile = avatarFile;
    }

    public long getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(long followersCount) {
        this.followersCount = followersCount;
    }

    public long getSubscriptionsCount() {
        return subscriptionsCount;
    }

    public void setSubscriptionsCount(long subscriptionsCount) {
        this.subscriptionsCount = subscriptionsCount;
    }

    public long getMeshesCount() {
        return meshesCount;
    }

    public void setMeshesCount(long meshesCount) {
        this.meshesCount = meshesCount;
    }

    public long getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    public JsonObject toJsonObject() {
        return Json.createObjectBuilder()
                .add("id", id)
                .add("name", name)
                .add("login", login)
                .add("avatar", avatarFile)
                .add("followersCount", followersCount)
                .add("subscriptionsCount", subscriptionsCount)
                .add("meshesCount", meshesCount)
                .add("likesCount", likesCount)
                .build();
    }
}
