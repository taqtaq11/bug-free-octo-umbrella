package org.octoteam.octoproject.Entities;

import org.jboss.resteasy.spi.touri.MappedBy;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.persistence.*;
import java.util.List;

/**
 * Created by Alexander on 18/02/16.
 */

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", length = 32, nullable = false)
    private String name;

    @Column(name = "login", length = 32, nullable = false)
    private String login;

    @Column(name = "password", length = 32, nullable = false)
    private String password;

    @Column(name = "avatar_file", length = 32, nullable = false)
    private String avatar_file;

    @OneToMany(mappedBy = "user")
    private List<Session> sessions;

    @ManyToMany
    @JoinTable(
            name = "followers",
            joinColumns = @JoinColumn(name = "follower_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    List<User> subscriptions;

    @ManyToMany(mappedBy = "subscriptions")
    List<User> followers;

    public User() {}

    public User(String name, String login, String password) {
        this.login = login;
        this.password = password;
        this.name = name;

        avatar_file = "no_avatar.jpg";
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getAvatar_file() {
        return avatar_file;
    }

    public void setAvatar_file(String avatar_file) {
        this.avatar_file = avatar_file;
    }

    public List<Session> getSessions() {
        return sessions;
    }

    public void setSessions(List<Session> sessions) {
        this.sessions = sessions;
    }

    public List<User> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(List<User> subscriptions) {
        this.subscriptions = subscriptions;
    }

    public List<User> getFollowers() {
        return followers;
    }

    public void setFollowers(List<User> followers) {
        this.followers = followers;
    }

    // todo meshes, subscriptions, followers
    public JsonObject toShortJSON() {
        return Json.createObjectBuilder()
                .add("id", id)
                .add("name", name)
                .add("login", login)
                .add("avatar_file", avatar_file)
                .add("subscriptions_count", subscriptions.size())
                .add("followers_count", followers.size())
                .build();
    }

    public JsonObject toFullJSON() {
        JsonArrayBuilder subscriptionsBuilder = Json.createArrayBuilder();
        for (User user : subscriptions) {
            subscriptionsBuilder.add(user.toShortJSON());
        }

        JsonArrayBuilder followersBuilder = Json.createArrayBuilder();
        for (User user : followers) {
            followersBuilder.add(user.toShortJSON());
        }

        return Json.createObjectBuilder()
                .add("id", id)
                .add("name", name)
                .add("login", login)
                .add("avatar_file", avatar_file)
                .add("subscriptions", subscriptionsBuilder.build())
                .add("followers", followersBuilder.build()).build();
    }
}
