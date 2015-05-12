package com.talentica.smsahu.chatroom.core;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.validator.constraints.Length;

import javax.ws.rs.QueryParam;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Message {
    @QueryParam("id")
    @JsonProperty
    private Long id;
    private final Date date;
    @JsonIgnore
    private ChatRoom chatRoom;
    @JsonProperty
    private User fromUser;
    @Length(max = 100)
    @QueryParam("content")
    @JsonProperty
    private String content;

    public Message() {
        // Jackson deserialization
        this.date = new Date();
    }

    public Message(ChatRoom chatRoom, User fromUser, String content) {
        this.date = new Date();
        this.chatRoom = chatRoom;
        this.fromUser = fromUser;
        this.content = content;
    }

    public Long getId() {
        if(null == id){
            id = (long)hashCode();
        }
        return id;
    }

    public Date getDate() {
        return date;
    }

    public ChatRoom getChatRoom() {
        return chatRoom;
    }

    public void setChatRoom(ChatRoom chatRoom) {
        this.chatRoom = chatRoom;
    }

    public User getFromUser() {
        return fromUser;
    }

    public void setFromUser(User fromUser) {
        this.fromUser = fromUser;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                //.add("id", id)
                .add("date", date)
                .add("content", content)
                .toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(getDate())
                .append(getChatRoom())
                .append(getFromUser())
                .append(getContent())
                .toHashCode();
    }
}
