package com.talentica.smsahu.chatroom;

import com.google.common.base.Optional;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import com.talentica.smsahu.chatroom.ChatRoomApplication;
import com.talentica.smsahu.chatroom.ChatRoomConfiguration;
import com.talentica.smsahu.chatroom.core.Message;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class IntegrationTest {

    private static final String TMP_FILE = createTempFile();
    private static final String CONFIG_PATH = ResourceHelpers.resourceFilePath("test-chatroom.yml");

    @ClassRule
    public static final DropwizardAppRule<ChatRoomConfiguration> RULE = new DropwizardAppRule<>(
            ChatRoomApplication.class, CONFIG_PATH);

    private Client client;


    @Before
    public void setUp() throws Exception {
        client = ClientBuilder.newClient();
    }

    @After
    public void tearDown() throws Exception {
        client.close();
    }

    private static String createTempFile() {
        try {
            return File.createTempFile("test-example", null).getAbsolutePath();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Test
    public void testGetChatRooms() throws Exception {
        final Optional<String> name = Optional.fromNullable("Dr. IntegrationTest");
        final Message saying = client.target("http://localhost:" + RULE.getLocalPort() + "/api/message/chatRoom")
                .queryParam("name", name.get())
                .request()
                .get(Message.class);
        assertThat(saying.getContent()).isEqualTo(RULE.getConfiguration().buildTemplate().render(name));
    }

    @Test
    public void testPostChat() throws Exception {
       /* final Person person = new Person("Dr. IntegrationTest", "Chief Wizard");
        final Person newPerson = client.target("http://localhost:" + RULE.getLocalPort() + "/api/people")
                .request()
                .post(Entity.entity(person, MediaType.APPLICATION_JSON_TYPE))
                .readEntity(Person.class);
        assertThat(newPerson.getId()).isNotNull();
        assertThat(newPerson.getFullName()).isEqualTo(person.getFullName());
        assertThat(newPerson.getJobTitle()).isEqualTo(person.getJobTitle());*/
    }
}
