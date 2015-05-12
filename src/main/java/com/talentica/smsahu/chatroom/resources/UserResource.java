package com.talentica.smsahu.chatroom.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.talentica.smsahu.chatroom.ChatRoomApplication;
import com.talentica.smsahu.chatroom.core.ChatRoom;
import com.talentica.smsahu.chatroom.core.User;
import com.talentica.smsahu.chatroom.persistence.UserRepository;
import io.dropwizard.auth.Auth;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.basic.BasicCredentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserResource.class);
    private final UserRepository userRepository;

    public UserResource(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GET
    public List<User> listUsers() {
        return Optional.of(userRepository.findAll()).orElse(null);
    }

    @GET
    @Path("/current")
    @RolesAllowed("ANY")
    public User showUser(@Auth User user) {
        return userRepository.findByName(user.getName()).orElse(null);
    }

    @GET
    @Path("/{userName}")
    public User showUser(@PathParam("userName") String userName) {
        return userRepository.findByName(userName).orElse(null);
    }

    @POST
    @Path("/login")
    @Consumes("application/x-www-form-urlencoded")
    public User login(@FormParam("username") String username, @FormParam("password") String password) throws AuthenticationException {
        LOGGER.info("Login Request: username>> " + username + " password>> " + password);
        com.google.common.base.Optional<User> response = ChatRoomApplication.getUserAuthenticator().authenticate(new BasicCredentials(username, password));
        if ( response.isPresent() ) {
            return response.get();
        } else {
            throw new WebApplicationException("Unable to log in with those credentials!");
        }
    }

    @PUT
    @Path("/{id}")
    public void updateChatRoom(@PathParam("id") Long userId, User user) {
        LOGGER.debug("user>> "+user);
        user.setId(userId);
        if(!userRepository.find(user).isPresent()){
            throw new WebApplicationException("USer not available", Response.Status.NOT_FOUND);
        } else {
            userRepository.update(user);
        }
    }

}
