package com.talentica.smsahu.chatroom.persistence;

import com.talentica.smsahu.chatroom.core.ChatRoom;
import com.talentica.smsahu.chatroom.core.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public class ChatRoomRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChatRoomRepository.class);

    private static List<ChatRoom> chatRoomList = new ArrayList<>();

    public ChatRoomRepository() {
    }

    public Optional<ChatRoom> find(ChatRoom chatRoom) {
        Optional<ChatRoom> chatRoomFromRepo = null;
        if(null != chatRoom) {
            if(null != chatRoom.getId()){
                chatRoomFromRepo = findById(chatRoom.getId());
            }else if(null != chatRoom.getName()){
                chatRoomFromRepo = findByName(chatRoom.getName()).stream().findFirst();
            }
        }
        return chatRoomFromRepo;
    }

    public Optional<ChatRoom> findById(Long id) {
        Optional<ChatRoom> chatRoom = chatRoomList.parallelStream()
                .filter(c -> c.getId().equals(id)).findFirst();
                //.max(Comparator.comparing(c -> c.getCreateDate()));
        return chatRoom;
    }

    public List<ChatRoom> findByName(String name) {
        List<ChatRoom> chatRooms = chatRoomList.parallelStream()
                .filter(c -> c.getName().equals(name))
                //.sorted(Comparator.comparing(c -> c.getCreateDate()))
                .collect(Collectors.toList());
        return chatRooms;
    }

    public ChatRoom create(ChatRoom chatRoom) {
        chatRoomList.add(chatRoom);
        return chatRoom;
    }

    public void update(ChatRoom chatRoom) {
        find(chatRoom).ifPresent(cr -> {
            /*chatRoomList.remove(cr);
            chatRoomList.add(chatRoom);*/
            cr.setName(chatRoom.getName());
        });
    }

    public void delete(ChatRoom chatRoom) {
        find(chatRoom).ifPresent(cr -> chatRoomList.remove(cr));
    }

    public boolean isUserInChatRoom(ChatRoom chatRoom, User user) {
        boolean isPresent = false;
        Optional<ChatRoom> chatRoomOpt = find(chatRoom);
        if(chatRoomOpt.isPresent()){
            isPresent = chatRoomOpt.get().getUsers().contains(user);
        }
        return isPresent;
    }

    public void addUserToChatRoom(ChatRoom chatRoom, User user) {
        find(chatRoom).ifPresent(cr -> cr.getUsers().add(user));
    }

    public void removeUserFromChatRoom(ChatRoom chatRoom, User user) {
        find(chatRoom).ifPresent(cr -> cr.getUsers().remove(user));
    }

    public List<ChatRoom> findAll() {
        return chatRoomList;
    }
}
