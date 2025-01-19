package org.gunnarro.microservice.todoservice.repository;

import org.gunnarro.microservice.todoservice.repository.entity.UserAccount;

import java.util.List;

public interface UserAccountRepository {

    UserAccount getUser(String userName);
    UserAccount getUser(Integer userId);
    /**
     *
     * @param user
     * @return
     */
    int createUser(UserAccount user);

    /**
     * @param user
     * @return
     */
    int updateUser(UserAccount user);

    /**
     * @param id
     * @return
     */
    int deleteUser(Integer id);

    /**
     * @return
     */
    List<String> getUserRoles();

    /**
     * @return
     */
    List<UserAccount> getUsers();

    /**
     * @param userId
     * @param password
     * @return
     */
    int changeUserPwd(Integer userId, String password);


    /**
     * @param userId
     * @return
     */
    void checkIfUserIsBlocked(Integer userId) throws SecurityException;

    /**
     * @param userId
     * @param numberOfAttemptFailures
     * @return
     */
    int updateUserLoginAttemptFailures(Integer userId, Integer numberOfAttemptFailures);

    /**
     * @param userId
     * @param numberOfAttemptSuccess
     * @return
     */
    int updateUserLoginAttemptSuccess(Integer userId, Integer numberOfAttemptSuccess);

}
