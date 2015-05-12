package com.talentica.smsahu.chatroom;

import com.codahale.metrics.MetricRegistry;
import com.google.common.base.Function;
import com.talentica.smsahu.chatroom.auth.UserAuthenticator;
import com.talentica.smsahu.chatroom.core.*;
import com.talentica.smsahu.chatroom.exception.WebExceptionMapper;
import com.talentica.smsahu.chatroom.health.TemplateHealthCheck;
import com.talentica.smsahu.chatroom.persistence.ChatRoomRepository;
import com.talentica.smsahu.chatroom.persistence.MessageRepository;
import com.talentica.smsahu.chatroom.persistence.UserRepository;
import com.talentica.smsahu.chatroom.resources.ChatRoomResource;
import com.talentica.smsahu.chatroom.resources.MessageResource;
import com.talentica.smsahu.chatroom.resources.UserResource;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthFilter;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.CachingAuthenticator;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.auth.basic.BasicCredentials;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

import javax.ws.rs.core.SecurityContext;
import java.security.Principal;

public class ChatRoomApplication extends Application<ChatRoomConfiguration> {
    public static void main(String[] args) throws Exception {
        new ChatRoomApplication().run(args);
    }

    private static final ChatRoomRepository chatRoomRepository = new ChatRoomRepository();
    private static final MessageRepository messageRepository = new MessageRepository();
    private static final UserRepository userRepository = new UserRepository();
    private static final UserAuthenticator userAuthenticator = new UserAuthenticator(userRepository);

    @Override
    public String getName() {
        return "chat-room";
    }

    public static UserAuthenticator getUserAuthenticator() {
        return userAuthenticator;
    }

    @Override
    public void initialize(Bootstrap<ChatRoomConfiguration> bootstrap) {
        // Enable variable substitution with environment variables
        bootstrap.setConfigurationSourceProvider(
                new SubstitutingSourceProvider(
                        bootstrap.getConfigurationSourceProvider(),
                        new EnvironmentVariableSubstitutor(false)
                )
        );

        //bootstrap.addBundle(new AssetsBundle("/assets/bower_components/", "/bower_components/"));
        bootstrap.addBundle(new AssetsBundle("/assets/", "/", "/app/angular/index.htm"));

        User user = new User("admin");
        ChatRoom chatRoom = new ChatRoom(Constant.DEFAULT_CHAT_ROOM_NAME.value(), user);
        chatRoomRepository.create(chatRoom);
        messageRepository.create(new Message(chatRoom, user, "Welcome to default chatRoom"));
    }

    @Override
    public void run(ChatRoomConfiguration configuration, Environment environment) {
        //environment.jersey().setUrlPattern("/api/*");
        final Template template = configuration.buildTemplate();

        environment.healthChecks().register("template", new TemplateHealthCheck(template));

        environment.jersey().register(new AuthDynamicFeature(new BasicCredentialAuthFilter.Builder<User, UserAuthenticator>()
                .setAuthenticator(userAuthenticator)
                .setSecurityContextFunction(getSecurityContextFunction())
                .setRealm("SUPER SECRET STUFF").buildAuthFilter()));

        /*CachingAuthenticator<BasicCredentials, User> cachingAuthenticator = new CachingAuthenticator<>(
                new MetricRegistry(), userAuthenticator,
                configuration.getAuthenticationCachePolicy());
        environment.jersey().register(new BasicAuthProvider<User>(cachedAuthenticator, "REALM MESSAGE"));*/

        environment.jersey().register(new AuthValueFactoryProvider.Binder(User.class));
        environment.jersey().register(RolesAllowedDynamicFeature.class);
        environment.jersey().register(new WebExceptionMapper());

        /*environment.jersey().register(new ViewResource());*/
        environment.jersey().register(new ChatRoomResource(chatRoomRepository, messageRepository, userRepository));
        environment.jersey().register(new MessageResource(messageRepository, chatRoomRepository, userRepository));
        environment.jersey().register(new UserResource(userRepository));
    }

    private Function<AuthFilter.Tuple, SecurityContext> getSecurityContextFunction() {
        return new Function<AuthFilter.Tuple, SecurityContext>() {
            @Override
            public SecurityContext apply(final AuthFilter.Tuple input) {
                return new SecurityContext() {

                    @Override
                    public Principal getUserPrincipal() {
                        return input.getPrincipal();
                    }

                    @Override
                    public boolean isUserInRole(String role) {
                        return true;
                    }

                    @Override
                    public boolean isSecure() {
                        return input.getContainerRequestContext().getSecurityContext().isSecure();
                    }

                    @Override
                    public String getAuthenticationScheme() {
                        return SecurityContext.BASIC_AUTH;
                    }
                };
            }
        };
    }
}
