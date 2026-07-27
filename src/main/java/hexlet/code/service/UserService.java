package hexlet.code.service;

import hexlet.code.dto.user.UserCreateDTO;
import hexlet.code.dto.user.UserDTO;
import hexlet.code.dto.user.UserUpdateDTO;

import java.util.List;

/**
 * User business operations.
 */
public interface UserService {

    /**
     * Returns all users.
     * @return list of users
     */
    List<UserDTO> getAll();

    /**
     * Returns a user by id.
     * @param id user id
     * @return user DTO
     */
    UserDTO getById(Long id);

    /**
     * Creates a user.
     * @param dto create data
     * @return created user
     */
    UserDTO create(UserCreateDTO dto);

    /**
     * Updates a user partially.
     * @param id user id
     * @param dto update data
     * @return updated user
     */
    UserDTO update(Long id, UserUpdateDTO dto);

    /**
     * Deletes a user.
     * @param id user id
     */
    void delete(Long id);
}
