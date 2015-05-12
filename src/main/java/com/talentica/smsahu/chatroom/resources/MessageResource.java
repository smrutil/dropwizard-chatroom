package com.talentica.smsahu.chatroom.resources;

import com.talentica.smsahu.chatroom.core.ChatRoom;
import com.talentica.smsahu.chatroom.core.Constant;
import com.talentica.smsahu.chatroom.core.Message;
import com.talentica.smsahu.chatroom.core.User;
import com.talentica.smsahu.chatroom.persistence.ChatRoomRepository;
import com.talentica.smsahu.chatroom.persistence.MessageRepository;
import com.talentica.smsahu.chatroom.persistence.UserRepository;
import io.dropwizard.auth.Auth;
import io.dropwizard.jersey.params.DateTimeParam;
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

@Path("/message")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MessageResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageResource.class);

    private final MessageRepository messageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    public MessageResource(MessageRepository messageRepository, ChatRoomRepository chatRoomRepository, UserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.chatRoomRepository = chatRoomRepository;
        this.userRepository = userRepository;
    }

    @GET
    @RolesAllowed("ANY")
    public List<Message> listMessages(@Auth User user) {
        List<Message> messages =  messageRepository.findAll();
        userRepository.updateLastVisit(user);
        LOGGER.info("messageRepository.findAll() >> " + messages);
        return messages;
    }

    @GET
    @Path("/chatRoom/{id}")
    @RolesAllowed("ANY")
    public List<Message> listChatRoomMessages(@Auth User user, @PathParam("id") Long chatRoomId) { //@BeanParam ChatRoom chatRoom
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

    @GET
    @Path("/chatRoom/{id}/fromLastSeen")
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

    @POST
    @RolesAllowed("ANY")
    public void saveMessage(@Auth User user, @Valid Message message) {
       saveMessage(user, null, message);
    }

    @POST
    @Path("/chatRoom/{id}")
    @RolesAllowed("ANY")
    public void saveMessage(@Auth User user, @PathParam("id") Long chatRoomId, @Valid Message message) {
        LOGGER.info("Received a message: {} from: {} ", message, user);
        message.setFromUser(user);

        if(null == message.getChatRoom() || null == message.getChatRoom().getId()) {
            message.setChatRoom(new ChatRoom(chatRoomId, Constant.DEFAULT_CHAT_ROOM_NAME.value(), user));
        }

        Optional<ChatRoom> chatRoomOpt = chatRoomRepository.find(message.getChatRoom());
        ChatRoom chatRoom;
        if(chatRoomOpt.isPresent()){
            chatRoom = chatRoomOpt.get();
        }else{
            //throw new WebApplicationException("ChatRoom not found ", Response.Status.NOT_FOUND);
            message.getChatRoom().setCreatedBy(user);
            chatRoom =  chatRoomRepository.create(message.getChatRoom());
        }
        if(!chatRoom.getUsers().contains(user)){
            //throw new WebApplicationException("User not in ChatRoom ", Response.Status.NOT_FOUND);
            chatRoomRepository.addUserToChatRoom(chatRoom, user);
        }
        message.setChatRoom(chatRoom);
        messageRepository.create(message);
    }

}
