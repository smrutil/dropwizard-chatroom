package com.talentica.smsahu.chatroom.auth;

import com.google.common.base.Optional;
import com.talentica.smsahu.chatroom.core.User;
import com.talentica.smsahu.chatroom.persistence.UserRepository;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;

public class UserAuthenticator implements Authenticator<BasicCredentials, User> {
    private final UserRepository userRepository;

    public UserAuthenticator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> authenticate(BasicCredentials credentials) throws AuthenticationException {
        if ("secret".equals(credentials.getPassword())) {

            User user = new User(credentials.getUsername());
            if(userRepository.find(user).isPresent()){
                user = userRepository.find(user).get();
            }else{
                user = userRepository.create(user);
            }

            return Optional.of(user);
        }
        return Optional.absent();
    }
}
