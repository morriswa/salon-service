package org.morriswa.dao;

import org.springframework.security.core.userdetails.User;

public interface UserProfileDao {

    User findUser(String username);

    void register(String username, String password) throws Exception;

    Long getUserId(String username);
}
