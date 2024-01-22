package org.morriswa.eecs447.dao;

import org.morriswa.eecs447.model.UserProfileResponse;
import org.springframework.security.core.userdetails.User;

public interface UserProfileDao {

    User findUser(String username);

    void register(String username, String password) throws Exception;

    UserProfileResponse getUserProfile(String username);
}
