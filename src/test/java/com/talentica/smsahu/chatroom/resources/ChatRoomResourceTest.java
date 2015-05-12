package com.talentica.smsahu.chatroom.resources;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.ImmutableList;
import com.talentica.smsahu.chatroom.ChatRoomApplication;
import com.talentica.smsahu.chatroom.ChatRoomConfiguration;
import com.talentica.smsahu.chatroom.core.User;
import com.talentica.smsahu.chatroom.persistence.MessageRepository;
import com.talentica.smsahu.chatroom.persistence.UserRepository;
import io.dropwizard.testing.junit.DropwizardAppRule;
import io.dropwizard.testing.junit.ResourceTestRule;
import com.talentica.smsahu.chatroom.core.ChatRoom;
import com.talentica.smsahu.chatroom.persistence.ChatRoomRepository;
import com.talentica.smsahu.chatroom.resources.ChatRoomResource;
import org.glassfish.jersey.test.grizzly.GrizzlyWebTestContainerFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link com.talentica.smsahu.chatroom.resources.ChatRoomResource}.
 */
public class ChatRoomResourceTest {
    private static final ChatRoomRepository chatRoomRepository = mock(ChatRoomRepository.class);
    private static final MessageRepository messageRepository = mock(MessageRepository.class);
    private static final UserRepository userRepository = mock(UserRepository.class);
    @ClassRule
    public static final ResourceTestRule RULE = ResourceTestRule.builder()
            .addResource(new ChatRoomResource(chatRoomRepository, messageRepository, userRepository))
            .setTestContainerFactory(new GrizzlyWebTestContainerFactory())
            .build();

    @ClassRule
    /*public static final DropwizardAppRule<ChatRoomConfiguration> RULE = new DropwizardAppRule<>(
            ChatRoomApplication.class, CONFIG_PATH);*/

    @Captor
    public static ArgumentCaptor<ChatRoom> chatRoomCaptor;

    private ChatRoom chatroom;

    @Before
    public void setup() {
        chatroom = new ChatRoom("test..", new User("test"));
    }

    @After
    public void tearDown() {
        reset(chatRoomRepository);
    }

    @Test
    public void createChatroom() throws JsonProcessingException {
        when(chatRoomRepository.create(any(ChatRoom.class))).thenReturn(chatroom);
        final Response response = RULE.client().target("http://localhost:8080/api/chatRoom")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(chatroom, MediaType.APPLICATION_JSON_TYPE));

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK);
        //verify(chatroom).create(chatRoomCaptor.capture());
        assertThat(chatRoomCaptor.getValue()).isEqualTo(chatroom);
    }

    @Test
    public void listPeople() throws Exception {
        final ImmutableList<ChatRoom> people = ImmutableList.of(chatroom);
        when(chatRoomRepository.findAll()).thenReturn(people);

        final List<ChatRoom> response = RULE.client().target("http://localhost:8080/api/chatRoom")
                .request().get(new GenericType<List<ChatRoom>>() {});

        verify(chatRoomRepository).findAll();
        assertThat(response).containsAll(people);
    }

    @Test
    public void getChatRoomSuccess() {
        when(chatRoomRepository.findById(1L)).thenReturn(Optional.of(chatroom));

        ChatRoom found = RULE.getJerseyTest().target("http://localhost:8080/api/chatRoom/1").request().get(ChatRoom.class);

        assertThat(found.getId()).isEqualTo(chatroom.getId());
        verify(chatRoomRepository).findById(1L);
    }

    @Test
    public void getChatRoomNotFound() {
        when(chatRoomRepository.findById(2L)).thenReturn(Optional.<ChatRoom>empty());
        final Response response = RULE.getJerseyTest().target("http://localhost:8080/api/chatRoom/2").request().get();

        assertThat(response.getStatus()).isEqualTo(Response.Status.NOT_FOUND.getStatusCode());
        verify(chatRoomRepository).findById(2L);
    }
}
