package hexlet.code.service;

import hexlet.code.dto.user.UserCreateDTO;
import hexlet.code.dto.user.UserDTO;
import hexlet.code.dto.user.UserUpdateDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.UserMapper;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Business logic for users.
 */
@Service
public class UserService {

    private static final String USER_NOT_FOUND = "User with id %s not found";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    /**
     * Returns all users.
     * @return list of users
     */
    public List<UserDTO> getAll() {
        return userRepository.findAll().stream()
                .map(userMapper::map)
                .toList();
    }

    /**
     * Returns a user by id.
     * @param id user id
     * @return user DTO
     */
    public UserDTO getById(Long id) {
        return userMapper.map(findUser(id));
    }

    /**
     * Creates a user.
     * @param dto create data
     * @return created user
     */
    public UserDTO create(UserCreateDTO dto) {
        User user = userMapper.map(dto);
        userRepository.save(user);
        return userMapper.map(user);
    }

    /**
     * Updates a user partially.
     * @param id user id
     * @param dto update data
     * @return updated user
     */
    public UserDTO update(Long id, UserUpdateDTO dto) {
        User user = findUser(id);
        userMapper.update(dto, user);
        userRepository.save(user);
        return userMapper.map(user);
    }

    /**
     * Deletes a user.
     * @param id user id
     */
    public void delete(Long id) {
        userRepository.delete(findUser(id));
    }

    private User findUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND.formatted(id)));
    }
}
