package models;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Alexander on 20/03/16.
 */
public class Mesh {
    private long id;
    private String name;
    private String description;
    private long author;
    private String objFile;
    private String textureFile;
    private long viewsCount;
    private long facesCount;
    private long verticesCount;

    private List<Long> likes;
    private List<Long> comments;

    public Mesh() {
        likes = new LinkedList<>();
        comments = new LinkedList<>();
    }

    public Mesh(long id, String name, String description, long author, String objFile,
                String textureFile, long viewsCount, long facesCount, long verticesCount) {
        this();

        this.id = id;
        this.name = name;
        this.description = description;
        this.author = author;
        this.objFile = objFile;
        this.textureFile = textureFile;
        this.viewsCount = viewsCount;
        this.facesCount = facesCount;
        this.verticesCount = verticesCount;
    }

    public long getVerticesCount() {
        return verticesCount;
    }

    public void setVerticesCount(long verticesCount) {
        this.verticesCount = verticesCount;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getAuthor() {
        return author;
    }

    public void setAuthor(long author) {
        this.author = author;
    }

    public String getObjFile() {
        return objFile;
    }

    public void setObjFile(String objFile) {
        this.objFile = objFile;
    }

    public String getTextureFile() {
        return textureFile;
    }

    public void setTextureFile(String textureFile) {
        this.textureFile = textureFile;
    }

    public long getViewsCount() {
        return viewsCount;
    }

    public void setViewsCount(long viewsCount) {
        this.viewsCount = viewsCount;
    }

    public long getFacesCount() {
        return facesCount;
    }

    public void setFacesCount(long facesCount) {
        this.facesCount = facesCount;
    }

    public List<Long> getLikes() {
        return likes;
    }

    public void setLikes(List<Long> likes) {
        this.likes = likes;
    }

    public List<Long> getComments() {
        return comments;
    }

    public void setComments(List<Long> comments) {
        this.comments = comments;
    }
}
