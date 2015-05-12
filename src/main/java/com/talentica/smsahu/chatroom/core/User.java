package com.talentica.smsahu.chatroom.core;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import io.dropwizard.auth.basic.BasicCredentials;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.ws.rs.QueryParam;
import java.security.Principal;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User implements Principal {
    @QueryParam("id")
    @JsonProperty
    private Long id;
    @QueryParam("name")
    @JsonProperty
    private String name;
    @JsonProperty
    private String nickName;
    @JsonIgnore
    private Date lastSeenDate;

    public User() {
    }

    public User(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getNickName() {
        if(StringUtils.isEmpty(nickName)){
            nickName = getName();
        }
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Date getLastSeenDate() {
        return lastSeenDate;
    }

    public void setLastSeenDate(Date lastSeenDate) {
        this.lastSeenDate = lastSeenDate;
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
                .toHashCode();
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("name", name)
                .add("nickName", nickName)
                .add("lastSeenDate", lastSeenDate)
                .toString();
    }
}
