package org.octoteam.octoproject.Entities;

import org.octoteam.octoproject.HashUtils;

import javax.persistence.*;
import java.util.Date;
import java.util.Random;

/**
 * Created by Alexander on 23/02/16.
 */

@Entity
@Table(name = "sessions")
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "ip", length = 32, nullable = false)
    private String ip;

    @Column(name = "token", length = 32, nullable = false)
    private String token;

    @Column(name = "last_access", nullable = false)
    private Date last_access;

    @Column(name = "is_open", nullable = false)
    private boolean is_open;

    public Session() {}

    public Session(User user, String ip) {
        this.user = user;
        this.ip = ip;

        token = HashUtils.md5(String.valueOf((new Random()).nextLong()));
        last_access = new Date();
        is_open = true;
    }

    public Date getLast_access() {
        return last_access;
    }

    public void setLast_access(Date last_acces) {
        this.last_access = last_acces;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean is_open() {
        return is_open;
    }

    public void setIs_open(boolean is_open) {
        this.is_open = is_open;
    }
}
