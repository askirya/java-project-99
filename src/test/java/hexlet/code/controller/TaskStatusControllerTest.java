package hexlet.code.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.TaskStatusRepository;
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
class TaskStatusControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private TaskStatus testStatus;
    private JwtRequestPostProcessor token;

    @BeforeEach
    void setUp() {
        taskStatusRepository.deleteAll();
        userRepository.deleteAll();

        User user = Instancio.of(User.class)
                .ignore(Select.field(User::getId))
                .ignore(Select.field(User::getCreatedAt))
                .ignore(Select.field(User::getUpdatedAt))
                .create();
        user.setEmail("status-user@google.com");
        user.setPasswordDigest(passwordEncoder.encode("password"));
        userRepository.save(user);
        token = jwt().jwt(builder -> builder.subject(user.getEmail()));

        testStatus = new TaskStatus();
        testStatus.setName("Draft");
        testStatus.setSlug("draft");
        taskStatusRepository.save(testStatus);
    }

    @Test
    void testIndexWithoutAuth() throws Exception {
        mockMvc.perform(get("/api/task_statuses"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testIndex() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/task_statuses").with(token))
                .andExpect(status().isOk())
                .andReturn();

        String body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray().isNotEmpty();
    }

    @Test
    void testShow() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/task_statuses/" + testStatus.getId()).with(token))
                .andExpect(status().isOk())
                .andReturn();

        String body = result.getResponse().getContentAsString();
        assertThatJson(body).and(
                v -> v.node("id").isEqualTo(testStatus.getId()),
                v -> v.node("name").isEqualTo("Draft"),
                v -> v.node("slug").isEqualTo("draft")
        );
    }

    @Test
    void testCreate() throws Exception {
        var data = new HashMap<String, String>();
        data.put("name", "New");
        data.put("slug", "new");

        MvcResult result = mockMvc.perform(post("/api/task_statuses")
                        .with(token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(data)))
                .andExpect(status().isCreated())
                .andReturn();

        String body = result.getResponse().getContentAsString();
        assertThatJson(body).and(
                v -> v.node("name").isEqualTo("New"),
                v -> v.node("slug").isEqualTo("new")
        );

        assertThat(taskStatusRepository.findBySlug("new")).isPresent();
    }

    @Test
    void testCreateWithInvalidData() throws Exception {
        var data = new HashMap<String, String>();
        data.put("name", "");
        data.put("slug", "");

        mockMvc.perform(post("/api/task_statuses")
                        .with(token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(data)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdate() throws Exception {
        var data = new HashMap<String, String>();
        data.put("name", "newStatus");

        MvcResult result = mockMvc.perform(put("/api/task_statuses/" + testStatus.getId())
                        .with(token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(data)))
                .andExpect(status().isOk())
                .andReturn();

        String body = result.getResponse().getContentAsString();
        assertThatJson(body).and(
                v -> v.node("name").isEqualTo("newStatus"),
                v -> v.node("slug").isEqualTo("draft")
        );

        TaskStatus status = taskStatusRepository.findById(testStatus.getId()).orElseThrow();
        assertThat(status.getName()).isEqualTo("newStatus");
        assertThat(status.getSlug()).isEqualTo("draft");
    }

    @Test
    void testDelete() throws Exception {
        mockMvc.perform(delete("/api/task_statuses/" + testStatus.getId()).with(token))
                .andExpect(status().isNoContent());

        assertThat(taskStatusRepository.existsById(testStatus.getId())).isFalse();
    }

    @Test
    void testFindBySlug() {
        assertThat(taskStatusRepository.findBySlug("draft")).isPresent();
        assertThat(taskStatusRepository.findBySlug("missing")).isEmpty();
    }
}
