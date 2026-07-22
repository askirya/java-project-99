package hexlet.code.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.TaskRepository;
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
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private User testUser;
    private TaskStatus draftStatus;
    private Task testTask;
    private JwtRequestPostProcessor token;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
        taskStatusRepository.deleteAll();
        userRepository.deleteAll();

        testUser = Instancio.of(User.class)
                .ignore(Select.field(User::getId))
                .ignore(Select.field(User::getCreatedAt))
                .ignore(Select.field(User::getUpdatedAt))
                .create();
        testUser.setEmail("task-user@google.com");
        testUser.setPasswordDigest(passwordEncoder.encode("password"));
        userRepository.save(testUser);
        token = jwt().jwt(builder -> builder.subject(testUser.getEmail()));

        draftStatus = new TaskStatus();
        draftStatus.setName("Draft");
        draftStatus.setSlug("draft");
        taskStatusRepository.save(draftStatus);

        TaskStatus reviewStatus = new TaskStatus();
        reviewStatus.setName("ToReview");
        reviewStatus.setSlug("to_review");
        taskStatusRepository.save(reviewStatus);

        testTask = new Task();
        testTask.setName("Task 1");
        testTask.setDescription("Description of task 1");
        testTask.setIndex(3140);
        testTask.setTaskStatus(draftStatus);
        testTask.setAssignee(testUser);
        taskRepository.save(testTask);
    }

    @Test
    void testIndexWithoutAuth() throws Exception {
        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testIndex() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/tasks").with(token))
                .andExpect(status().isOk())
                .andReturn();

        String body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray().isNotEmpty();
    }

    @Test
    void testShow() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/tasks/" + testTask.getId()).with(token))
                .andExpect(status().isOk())
                .andReturn();

        String body = result.getResponse().getContentAsString();
        assertThatJson(body).and(
                v -> v.node("id").isEqualTo(testTask.getId()),
                v -> v.node("title").isEqualTo("Task 1"),
                v -> v.node("content").isEqualTo("Description of task 1"),
                v -> v.node("status").isEqualTo("draft"),
                v -> v.node("assignee_id").isEqualTo(testUser.getId()),
                v -> v.node("index").isEqualTo(3140)
        );
    }

    @Test
    void testCreate() throws Exception {
        var data = new HashMap<String, Object>();
        data.put("index", 12);
        data.put("assignee_id", testUser.getId());
        data.put("title", "Test title");
        data.put("content", "Test content");
        data.put("status", "draft");

        MvcResult result = mockMvc.perform(post("/api/tasks")
                        .with(token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(data)))
                .andExpect(status().isCreated())
                .andReturn();

        String body = result.getResponse().getContentAsString();
        assertThatJson(body).and(
                v -> v.node("title").isEqualTo("Test title"),
                v -> v.node("content").isEqualTo("Test content"),
                v -> v.node("status").isEqualTo("draft"),
                v -> v.node("assignee_id").isEqualTo(testUser.getId()),
                v -> v.node("index").isEqualTo(12)
        );

        assertThat(taskRepository.findAll()).hasSize(2);
    }

    @Test
    void testCreateWithInvalidData() throws Exception {
        var data = new HashMap<String, Object>();
        data.put("title", "");
        data.put("status", "draft");

        mockMvc.perform(post("/api/tasks")
                        .with(token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(data)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdate() throws Exception {
        var data = new HashMap<String, Object>();
        data.put("title", "New title");
        data.put("content", "New content");
        data.put("status", "to_review");

        MvcResult result = mockMvc.perform(put("/api/tasks/" + testTask.getId())
                        .with(token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(data)))
                .andExpect(status().isOk())
                .andReturn();

        String body = result.getResponse().getContentAsString();
        assertThatJson(body).and(
                v -> v.node("title").isEqualTo("New title"),
                v -> v.node("content").isEqualTo("New content"),
                v -> v.node("status").isEqualTo("to_review"),
                v -> v.node("assignee_id").isEqualTo(testUser.getId())
        );

        Task task = taskRepository.findById(testTask.getId()).orElseThrow();
        assertThat(task.getName()).isEqualTo("New title");
        assertThat(task.getDescription()).isEqualTo("New content");
        assertThat(task.getTaskStatus().getSlug()).isEqualTo("to_review");
    }

    @Test
    void testDelete() throws Exception {
        mockMvc.perform(delete("/api/tasks/" + testTask.getId()).with(token))
                .andExpect(status().isNoContent());

        assertThat(taskRepository.existsById(testTask.getId())).isFalse();
    }

    @Test
    void testCannotDeleteUserAssignedToTask() throws Exception {
        mockMvc.perform(delete("/api/users/" + testUser.getId()).with(token))
                .andExpect(status().isConflict());

        assertThat(userRepository.existsById(testUser.getId())).isTrue();
    }

    @Test
    void testCannotDeleteStatusUsedByTask() throws Exception {
        mockMvc.perform(delete("/api/task_statuses/" + draftStatus.getId()).with(token))
                .andExpect(status().isConflict());

        assertThat(taskStatusRepository.existsById(draftStatus.getId())).isTrue();
    }
}
