package hexlet.code.component;

import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Seeds initial admin user on application startup.
 */
@Component
public class DataInitializer implements ApplicationRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Runs data initialization.
     * @param args application arguments
     */
    @Override
    public void run(ApplicationArguments args) {
        createAdmin();
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
}
