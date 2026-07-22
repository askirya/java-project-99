package hexlet.code.component;

import hexlet.code.model.Label;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Seeds initial admin user, default task statuses and labels on application startup.
 */
@Component
public class DataInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final TaskStatusRepository taskStatusRepository;
    private final LabelRepository labelRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Creates data initializer.
     * @param userRepository users repository
     * @param taskStatusRepository statuses repository
     * @param labelRepository labels repository
     * @param passwordEncoder password encoder
     */
    public DataInitializer(
            UserRepository userRepository,
            TaskStatusRepository taskStatusRepository,
            LabelRepository labelRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.taskStatusRepository = taskStatusRepository;
        this.labelRepository = labelRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Runs data initialization.
     * @param args application arguments
     */
    @Override
    public void run(ApplicationArguments args) {
        createAdmin();
        createDefaultTaskStatuses();
        createDefaultLabels();
    }

    private void createAdmin() {
        if (userRepository.findByEmail("hexlet@example.com").isPresent()) {
            return;
        }

        User user = new User();
        user.setEmail("hexlet@example.com");
        user.setPasswordDigest(passwordEncoder.encode("qwerty"));
        user.setFirstName("Admin");
        user.setLastName("Hexlet");
        userRepository.save(user);
    }

    private void createDefaultTaskStatuses() {
        Map<String, String> statuses = new LinkedHashMap<>();
        statuses.put("Draft", "draft");
        statuses.put("ToReview", "to_review");
        statuses.put("ToBeFixed", "to_be_fixed");
        statuses.put("ToPublish", "to_publish");
        statuses.put("Published", "published");

        for (Map.Entry<String, String> entry : statuses.entrySet()) {
            if (taskStatusRepository.findBySlug(entry.getValue()).isPresent()) {
                continue;
            }
            TaskStatus status = new TaskStatus();
            status.setName(entry.getKey());
            status.setSlug(entry.getValue());
            taskStatusRepository.save(status);
        }
    }

    private void createDefaultLabels() {
        for (String name : List.of("feature", "bug")) {
            if (labelRepository.findByName(name).isPresent()) {
                continue;
            }
            Label label = new Label();
            label.setName(name);
            labelRepository.save(label);
        }
    }
}
