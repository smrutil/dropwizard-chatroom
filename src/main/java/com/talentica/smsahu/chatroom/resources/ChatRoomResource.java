package com.talentica.smsahu.chatroom.resources;

import com.talentica.smsahu.chatroom.core.ChatRoom;
import com.talentica.smsahu.chatroom.core.Constant;
import com.talentica.smsahu.chatroom.core.Message;
import com.talentica.smsahu.chatroom.core.User;
import com.talentica.smsahu.chatroom.persistence.ChatRoomRepository;
import com.talentica.smsahu.chatroom.persistence.MessageRepository;
import com.talentica.smsahu.chatroom.persistence.UserRepository;
import io.dropwizard.auth.Auth;
import io.dropwizard.jersey.params.LongParam;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Path("/chatRoom")
@Produces(MediaType.APPLICATION_JSON)
public class ChatRoomResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageResource.class);

    private final ChatRoomRepository chatRoomRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    public ChatRoomResource(ChatRoomRepository chatRoomRepository, MessageRepository messageRepository, UserRepository userRepository) {
        this.chatRoomRepository = chatRoomRepository;
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
    }

    @GET
    public List<ChatRoom> listChatRooms() {
        return chatRoomRepository.findAll();
    }

    @GET
    @Path("/{id}")
    public ChatRoom getChatRoom(@PathParam("id") Long chatRoomId) {
        ChatRoom chatRoom = new ChatRoom(chatRoomId);
        Optional<ChatRoom> chatRoomOpt = chatRoomRepository.find(chatRoom);
        if(!chatRoomOpt.isPresent()){
            throw new WebApplicationException("ChatRoom not available", Response.Status.NOT_FOUND);
        } else {
            return chatRoomOpt.get();
        }
    }

    /*@GET
    @Path("/messages")
    @RolesAllowed("ANY")
    public List<Message> listChatRoomMessages(@Auth User user) {
        return  listChatRoomMessages(user, null);
    }*/

    @GET
    @Path("{id}/messages")
    @RolesAllowed("ANY")
    public List<Message> listChatRoomMessages(@Auth User user, @PathParam("id") Long chatRoomId) {
        List<Message> messages;
        ChatRoom chatRoom = new ChatRoom(chatRoomId, null, user);
        LOGGER.info("Get listChatRoomMessages from: {}", chatRoom);

        if(!chatRoomRepository.find(chatRoom).isPresent()){
            throw new WebApplicationException("ChatRoom not available", Response.Status.NOT_FOUND);
        } else {
            messages = messageRepository.findByChatRoom(chatRoom);
            userRepository.updateLastVisit(user);
        }
        return messages;
    }


    /*@GET
    @Path("/messages/fromLastSeen")
    @RolesAllowed("ANY")
    public List<Message> listChatRoomMessagesFromLastSeen(@Auth User user) {
        return listChatRoomMessagesFromLastSeen(user, null);
    }*/

    @GET
    @Path("{id}/messages/fromLastSeen")
    @RolesAllowed("ANY")
    public List<Message> listChatRoomMessagesFromLastSeen(@Auth User user, @PathParam("id") Long chatRoomId) {
        List<Message> messages;
        ChatRoom chatRoom = new ChatRoom(chatRoomId, null, user);

        LOGGER.info("Get listChatRoomMessagesFromLastSeen from: {} for {} ", chatRoom, user);
        if(!chatRoomRepository.find(chatRoom).isPresent()){
            throw new WebApplicationException("ChatRoom not available", Response.Status.NOT_FOUND);
        } else {
            messages = messageRepository.findByChatRoomUserLastSeen(chatRoom, user);
            userRepository.updateLastVisit(user);
        }
        return messages;
    }


    /*@GET
    @Path("/users")
    @RolesAllowed("ANY")
    public Set<User> listChatRoomUsers(@Auth User user) {
        return listChatRoomUsers(user, null);
    }*/

    @GET
    @Path("{id}/users")
    @RolesAllowed("ANY")
    public Set<User> listChatRoomUsers(@Auth User user, @PathParam("id") Long chatRoomId) {
        Set<User> users;
        ChatRoom chatRoom = new ChatRoom(chatRoomId, null, user);
        LOGGER.info("Get listChatRoomUsers from: {}", chatRoom);

        if(!chatRoomRepository.find(chatRoom).isPresent()){
            throw new WebApplicationException("ChatRoom not available", Response.Status.NOT_FOUND);
        } else {
            users = chatRoomRepository.find(chatRoom).get().getUsers();
            userRepository.updateLastVisit(user);
        }
        return users;
    }


    @POST
    @RolesAllowed("ANY")
    public void createChatRoom(@Auth User user, @Valid ChatRoom chatRoom) {
        if(chatRoomRepository.find(chatRoom).isPresent()){
            throw new WebApplicationException("ChatRoom already available", Response.Status.CONFLICT);
        } else {
            chatRoom.setCreatedBy(user);
            chatRoomRepository.create(chatRoom);
        }
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed("ANY")
    public void updateChatRoom(@Auth User user, @PathParam("id") Long chatRoomId, @Valid ChatRoom chatRoom) {
        //ChatRoom chatRoomById = new ChatRoom();
        chatRoom.setId(chatRoomId);
        if(!chatRoomRepository.find(chatRoom).isPresent()){
            throw new WebApplicationException("ChatRoom not available", Response.Status.NOT_FOUND);
        } else {
            chatRoomRepository.update(chatRoom);
        }
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("ANY")
    public void deleteChatRoom(@Auth User user, @PathParam("id") Long chatRoomId) {
        ChatRoom chatRoom = new ChatRoom(chatRoomId);
        if(!chatRoomRepository.find(chatRoom).isPresent()){
            throw new WebApplicationException("ChatRoom not available", Response.Status.NOT_FOUND);
        } else {
            //chatRoom.setDeletedBy(user);
            chatRoomRepository.delete(chatRoom);
        }
    }

    @POST
    @Path("/join")
    @RolesAllowed("ANY")
    public void joinChatRoom(@Auth User user, @Valid ChatRoom chatRoom){
        if(!chatRoomRepository.find(chatRoom).isPresent()){
            throw new WebApplicationException("No such ChatRoom available", Response.Status.NOT_FOUND);
        } else if(chatRoomRepository.isUserInChatRoom(chatRoom, user)){
            throw new WebApplicationException("User already in ChatRoom", Response.Status.CONFLICT);
        } else {
            chatRoomRepository.addUserToChatRoom(chatRoom, user);
        }
    }

    @POST
    @Path("/leave")
    @RolesAllowed("ANY")
    public void leaveChatRoom(@Auth User user, @Valid ChatRoom chatRoom){
        if(!chatRoomRepository.find(chatRoom).isPresent()){
            throw new WebApplicationException("No such ChatRoom available", Response.Status.NOT_FOUND);
        } else if(!chatRoomRepository.isUserInChatRoom(chatRoom, user)){
            throw new WebApplicationException("User not in ChatRoom", Response.Status.NOT_FOUND);
        } else {
            chatRoomRepository.removeUserFromChatRoom(chatRoom, user);
        }
    }

}
