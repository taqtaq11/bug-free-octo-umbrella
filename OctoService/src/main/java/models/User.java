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

    private List<Long> meshes;

    private List<Long> subscriptions;
    private List<Long> followers;

    private List<Long> likes;
    private List<Long> comments;

    public User() {
        meshes = new LinkedList<>();

        subscriptions = new LinkedList<>();
        followers = new LinkedList<>();

        likes = new LinkedList<>();
        comments = new LinkedList<>();
    }


    public User(long id, String login, String password, String name, String avatarFile) {
        this();

        this.id = id;
        this.login = login;
        this.password = password;
        this.name = name;
        this.avatarFile = avatarFile;
    }

    public List<Long> getComments() {
        return comments;
    }

    public void setComments(List<Long> comments) {
        this.comments = comments;
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

    public List<Long> getMeshes() {
        return meshes;
    }

    public void setMeshes(List<Long> meshes) {
        this.meshes = meshes;
    }

    public List<Long> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(List<Long> subscriptions) {
        this.subscriptions = subscriptions;
    }

    public List<Long> getFollowers() {
        return followers;
    }

    public void setFollowers(List<Long> followers) {
        this.followers = followers;
    }

    public List<Long> getLikes() {
        return likes;
    }

    public void setLikes(List<Long> likes) {
        this.likes = likes;
    }

    public JsonObject toJsonObject() {
        JsonObjectBuilder result = Json.createObjectBuilder()
                .add("id", id)
                .add("login", login)
                .add("name", name)
                .add("avatarFile", avatarFile);

        JsonArrayBuilder meshesJson = Json.createArrayBuilder();
        meshes.forEach(x -> meshesJson.add(x));
        result.add("meshes", meshesJson.build());

        JsonArrayBuilder subscriptionsJson = Json.createArrayBuilder();
        subscriptions.forEach(x -> subscriptionsJson.add(x));
        result.add("subscriptions", subscriptionsJson.build());

        JsonArrayBuilder followersJson = Json.createArrayBuilder();
        followers.forEach(x -> followersJson.add(x));
        result.add("followers", followersJson.build());

        JsonArrayBuilder likesJson = Json.createArrayBuilder();
        likes.forEach(x -> likesJson.add(x));
        result.add("likes", likesJson.build());

        JsonArrayBuilder commentsJson = Json.createArrayBuilder();
        likes.forEach(x -> commentsJson.add(x));
        result.add("comments", commentsJson.build());

        return result.build();
    }

}
