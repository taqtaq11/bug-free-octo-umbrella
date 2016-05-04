package models;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Alexander on 20/03/16.
 */

public class User {
    private long id;
    private String login;
    private String password;
    private String name;
    private String avatarFile;

    private List<UserShort> subscriptions;
    private List<UserShort> followers;

    private List<Mesh> meshes;
    private List<Mesh> likes;

    public User() {
        followers = new LinkedList<>();
        meshes = new LinkedList<>();
        likes = new LinkedList<>();
        subscriptions = new LinkedList<>();
    }

    public User(long id, String login, String password, String name, String avatar_file) {
        this();

        this.id = id;
        this.login = login;
        this.password = password;
        this.name = name;
        this.avatarFile = avatar_file;
    }

    public List<UserShort> getFollowers() {
        return followers;
    }

    public List<UserShort> getSubscriptions() {
        return subscriptions;
    }

    public List<Mesh> getMeshes() {
        return meshes;
    }

    public List<Mesh> getLikes() {
        return likes;
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

    public JsonObject toJsonObject() {
        JsonObjectBuilder result = Json.createObjectBuilder()
                .add("id", id)
                .add("name", name)
                .add("login", login)
                .add("avatar", avatarFile);

        JsonArrayBuilder followersJson = Json.createArrayBuilder();
        followers.forEach(x -> followersJson.add(x.toJsonObject()));
        result.add("followers", followersJson.build());

        JsonArrayBuilder subscriptionsJson = Json.createArrayBuilder();
        subscriptions.forEach(x -> subscriptionsJson.add(x.toJsonObject()));
        result.add("subscriptions", subscriptionsJson.build());

        JsonArrayBuilder meshesJson = Json.createArrayBuilder();
        meshes.forEach(x -> meshesJson.add(x.toJsonObject()));
        result.add("subscriptions", meshesJson.build());

        JsonArrayBuilder likesJson = Json.createArrayBuilder();
        likes.forEach(x -> likesJson.add(x.toJsonObject()));
        result.add("subscriptions", likesJson.build());

        return result.build();
    }

}
