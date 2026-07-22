package hexlet.code.component;

import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Seeds initial admin user and default task statuses on application startup.
 */
@Component
public class DataInitializer implements ApplicationRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Runs data initialization.
     * @param args application arguments
     */
    @Override
    public void run(ApplicationArguments args) {
        createAdmin();
        createDefaultTaskStatuses();
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
}
