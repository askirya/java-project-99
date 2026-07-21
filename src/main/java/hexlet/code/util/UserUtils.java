package hexlet.code.util;

import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Helpers for current authenticated user.
 */
@Component
public class UserUtils {

    @Autowired
    private UserRepository userRepository;

    /**
     * Returns currently authenticated user.
     * @return current user or null
     */
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        String email = authentication.getName();
        return userRepository.findByEmail(email).orElse(null);
    }

    /**
     * Checks whether current user owns the given id.
     * @param id user id
     * @return true if owner
     */
    public boolean isOwner(Long id) {
        User currentUser = getCurrentUser();
        return currentUser != null && currentUser.getId().equals(id);
    }
}
