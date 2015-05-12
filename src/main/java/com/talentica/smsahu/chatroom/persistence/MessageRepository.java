package com.talentica.smsahu.chatroom.persistence;

import com.talentica.smsahu.chatroom.core.ChatRoom;
import com.talentica.smsahu.chatroom.core.Message;
import com.talentica.smsahu.chatroom.core.User;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MessageRepository {
    private static List<Message> messageList = new ArrayList<>();
    public MessageRepository() {
    }

    /*List<Message> messageListById = messageList.parallelStream()
        .filter(m -> m.getId() == id)
        .collect(Collectors.toList());*/
    //(m1, m2) -> m1.getDate().compareTo(m2.getDate())

    public Optional<Message> findById(Long id) {
        Optional<Message> message = messageList.parallelStream()
                .filter(m -> m.getId() == id)
                .max(Comparator.comparing(m -> m.getDate()));
        return message;
    }

    public List<Message> findByFromUser(User user) {
        List<Message> messages = messageList.parallelStream()
                .filter(m -> m.getFromUser().getName().equals(user.getName()))
                .sorted(Comparator.comparing(m -> m.getDate()))
                .collect(Collectors.toList());
        return messages;
    }

    public List<Message> findByChatRoom(ChatRoom chatRoom) {
        List<Message> messages = messageList.parallelStream()
                .filter(m -> m.getChatRoom().getId().equals(chatRoom.getId()))
                .sorted(Comparator.comparing(m -> m.getDate()))
                .collect(Collectors.toList());
        return messages;
    }

    public List<Message> findByChatRoomUserLastSeen(ChatRoom chatRoom, User user) {
        List<Message> messages = messageList.parallelStream()
                .filter(m -> m.getChatRoom().getId().equals(chatRoom.getId()))
                .filter(m -> user.getLastSeenDate()!=null? m.getDate().after(user.getLastSeenDate()): true)
                .sorted(Comparator.comparing(m -> m.getDate()))
                .collect(Collectors.toList());
        return messages;
    }

    public Message create(Message message) {
        messageList.add(message);
        return message;
    }

    public List<Message> findAll() {
        return messageList;
    }
}
