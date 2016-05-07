package models;

import java.sql.Timestamp;

/**
 * Created by Alexander on 07/05/16.
 */
public class Comment {
    private long id;
    private long userId;
    private long meshId;
    private String message;
    private Timestamp timestamp;
    public Comment() {}

    public Comment(long id, long userId, long meshId, String message, Timestamp timestamp) {
        this.id = id;
        this.userId = userId;
        this.meshId = meshId;
        this.message = message;
        this.timestamp = timestamp;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getMeshId() {
        return meshId;
    }

    public void setMeshId(long meshId) {
        this.meshId = meshId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
