package com.talentica.smsahu.chatroom.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.ws.rs.QueryParam;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ChatRoom {
    @QueryParam("id")
    @JsonProperty
    private Long id;
    //@PathParam("name")
    @QueryParam("name")
    @JsonProperty
    private String name;
    private Set<User> users = new HashSet<>();
    private Date createDate;
    private User createdBy;

    public ChatRoom() {
        this.createDate = new Date();
    }

    public ChatRoom(Long id) {
        this.id = id;
        this.createDate = new Date();
    }

    public ChatRoom(String name, User createdBy) {
        this.name = name;
        this.createDate = new Date();
        this.createdBy = createdBy;
    }

    public ChatRoom(Long id, String name, User createdBy) {
        this.id = id;
        this.name = name;
        this.createdBy = createdBy;
        this.createDate = new Date();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<User> getUsers() {
        return users;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public Long getId() {
        if(null == id){
            id = (long)hashCode();
        }
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(getName())
                //.append(getCreateDate())
                .toHashCode();
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", getId())
                .add("name", name)
                .toString();
    }

}
