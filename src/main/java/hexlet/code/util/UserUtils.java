package hexlet.code.util;

import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

/**
 * Helpers for current authenticated user.
 */
@Component
public class UserUtils {

    private final UserRepository userRepository;

    /**
     * Creates user utils.
     * @param userRepository users repository
     */
    public UserUtils(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Returns user for the given authentication.
     * @param authentication security authentication
     * @return current user or null
     */
    public User getCurrentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        return userRepository.findByEmail(authentication.getName()).orElse(null);
    }

    /**
     * Checks whether authenticated user owns the given id.
     * @param authentication security authentication
     * @param id user id
     * @return true if owner
     */
    public boolean isOwner(Authentication authentication, Long id) {
        User currentUser = getCurrentUser(authentication);
        return currentUser != null && currentUser.getId().equals(id);
    }
}
