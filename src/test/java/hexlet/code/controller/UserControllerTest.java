package hexlet.code.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.model.User;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.UserRepository;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.HashMap;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private User testUser;
    private JwtRequestPostProcessor token;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
        userRepository.deleteAll();

        testUser = Instancio.of(User.class)
                .ignore(Select.field(User::getId))
                .ignore(Select.field(User::getCreatedAt))
                .ignore(Select.field(User::getUpdatedAt))
                .create();
        testUser.setEmail("john@google.com");
        testUser.setPasswordDigest(passwordEncoder.encode("password"));
        userRepository.save(testUser);

        token = jwt().jwt(builder -> builder.subject(testUser.getEmail()));
    }

    @Test
    void testIndexWithoutAuth() throws Exception {
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testIndex() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/users").with(token))
                .andExpect(status().isOk())
                .andReturn();

        String body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray().isNotEmpty();
        assertThat(body).doesNotContain("password");
        assertThat(body).doesNotContain("passwordDigest");
    }

    @Test
    void testShow() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/users/" + testUser.getId()).with(token))
                .andExpect(status().isOk())
                .andReturn();

        String body = result.getResponse().getContentAsString();
        assertThatJson(body).and(
                v -> v.node("id").isEqualTo(testUser.getId()),
                v -> v.node("email").isEqualTo(testUser.getEmail()),
                v -> v.node("firstName").isEqualTo(testUser.getFirstName()),
                v -> v.node("lastName").isEqualTo(testUser.getLastName())
        );
        assertThat(body).doesNotContain("password");
        assertThat(body).doesNotContain("passwordDigest");
    }

    @Test
    void testShowNotFound() throws Exception {
        mockMvc.perform(get("/api/users/99999").with(token))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreate() throws Exception {
        var data = new HashMap<String, String>();
        data.put("email", "jack@google.com");
        data.put("firstName", "Jack");
        data.put("lastName", "Jons");
        data.put("password", "some-password");

        MvcResult result = mockMvc.perform(post("/api/users")
                        .with(token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(data)))
                .andExpect(status().isCreated())
                .andReturn();

        String body = result.getResponse().getContentAsString();
        assertThatJson(body).and(
                v -> v.node("email").isEqualTo("jack@google.com"),
                v -> v.node("firstName").isEqualTo("Jack"),
                v -> v.node("lastName").isEqualTo("Jons")
        );
        assertThat(body).doesNotContain("password");

        User user = userRepository.findByEmail("jack@google.com").orElseThrow();
        assertThat(user.getFirstName()).isEqualTo("Jack");
        assertThat(passwordEncoder.matches("some-password", user.getPasswordDigest())).isTrue();
    }

    @Test
    void testCreateWithInvalidData() throws Exception {
        var data = new HashMap<String, String>();
        data.put("email", "not-an-email");
        data.put("password", "ab");

        mockMvc.perform(post("/api/users")
                        .with(token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(data)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdate() throws Exception {
        var data = new HashMap<String, String>();
        data.put("email", "jack@yahoo.com");
        data.put("password", "new-password");

        String oldFirstName = testUser.getFirstName();
        String oldLastName = testUser.getLastName();

        MvcResult result = mockMvc.perform(put("/api/users/" + testUser.getId())
                        .with(token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(data)))
                .andExpect(status().isOk())
                .andReturn();

        String body = result.getResponse().getContentAsString();
        assertThatJson(body).and(
                v -> v.node("email").isEqualTo("jack@yahoo.com"),
                v -> v.node("firstName").isEqualTo(oldFirstName),
                v -> v.node("lastName").isEqualTo(oldLastName)
        );

        User user = userRepository.findById(testUser.getId()).orElseThrow();
        assertThat(user.getEmail()).isEqualTo("jack@yahoo.com");
        assertThat(user.getFirstName()).isEqualTo(oldFirstName);
        assertThat(passwordEncoder.matches("new-password", user.getPasswordDigest())).isTrue();
    }

    @Test
    void testUpdateAnotherUserForbidden() throws Exception {
        User another = Instancio.of(User.class)
                .ignore(Select.field(User::getId))
                .ignore(Select.field(User::getCreatedAt))
                .ignore(Select.field(User::getUpdatedAt))
                .create();
        another.setEmail("other@google.com");
        another.setPasswordDigest(passwordEncoder.encode("password"));
        userRepository.save(another);

        var data = new HashMap<String, String>();
        data.put("firstName", "Hacker");

        mockMvc.perform(put("/api/users/" + another.getId())
                        .with(token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(data)))
                .andExpect(status().isForbidden());
    }

    @Test
    void testDelete() throws Exception {
        mockMvc.perform(delete("/api/users/" + testUser.getId()).with(token))
                .andExpect(status().isNoContent());

        assertThat(userRepository.existsById(testUser.getId())).isFalse();
    }

    @Test
    void testLogin() throws Exception {
        var data = new HashMap<String, String>();
        data.put("username", testUser.getEmail());
        data.put("password", "password");

        MvcResult result = mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(data)))
                .andExpect(status().isOk())
                .andReturn();

        String tokenValue = result.getResponse().getContentAsString();
        assertThat(tokenValue).isNotBlank();
    }

    @Test
    void testLoginWithInvalidCredentials() throws Exception {
        var data = new HashMap<String, String>();
        data.put("username", testUser.getEmail());
        data.put("password", "wrong-password");

        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(data)))
                .andExpect(status().isUnauthorized());
    }
}
