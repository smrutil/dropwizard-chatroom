package com.talentica.smsahu.chatroom.persistence;

import com.talentica.smsahu.chatroom.core.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class UserRepository {

    private static List<User> userList = new ArrayList<>();
    public UserRepository() {
    }

    public Optional<User> find(User user) {
        Optional<User> userFromRepo = null;
        if(null != user) {
            if(null != user.getId()){
                userFromRepo = findById(user.getId());
            }else if(null != user.getName()){
                userFromRepo = findByName(user.getName());
            }
        }
        return userFromRepo;
    }

    public Optional<User> findById(Long id) {
        Optional<User> user = userList.parallelStream()
                .filter(c -> c.getId().equals(id))
                .findFirst();
        return user;
    }

    public Optional<User> findByName(String name) {
        Optional<User> user = userList.parallelStream()
                .filter(c -> c.getName().equals(name))
                .findFirst();
        return user;
    }

    public User create(User user) {
        userList.add(user);
        return user;
    }

    public void update(User user) {
        find(user).ifPresent(us -> {
            /*chatRoomList.remove(cr);
            chatRoomList.add(chatRoom);*/
            us.setNickName(user.getNickName());
        });
    }
    public List<User> findAll() {
        return userList;
    }

    public User updateLastVisit(User user) {
        if(null != user) {
            User userFromRepository = find(user).orElse(null);
            if (null != userFromRepository) {
                user = userFromRepository;
            }/* else {
                user = create(user);
            }*/
            user.setLastSeenDate(new Date());
        }
        return user;
    }
}
