package hexlet.code.service;

import hexlet.code.dto.user.UserCreateDTO;
import hexlet.code.dto.user.UserDTO;
import hexlet.code.dto.user.UserUpdateDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.UserMapper;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Default user service implementation.
 */
@Service
public class UserServiceImpl implements UserService {

    private static final String USER_NOT_FOUND = "User with id %s not found";

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    /**
     * Creates user service.
     * @param userRepository users repository
     * @param userMapper user mapper
     */
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UserDTO> getAll() {
        return userRepository.findAll().stream()
                .map(userMapper::map)
                .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserDTO getById(Long id) {
        return userMapper.map(findUser(id));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserDTO create(UserCreateDTO dto) {
        User user = userMapper.map(dto);
        userRepository.save(user);
        return userMapper.map(user);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserDTO update(Long id, UserUpdateDTO dto) {
        User user = findUser(id);
        userMapper.update(dto, user);
        userRepository.save(user);
        return userMapper.map(user);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(Long id) {
        userRepository.delete(findUser(id));
    }

    private User findUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND.formatted(id)));
    }
}
