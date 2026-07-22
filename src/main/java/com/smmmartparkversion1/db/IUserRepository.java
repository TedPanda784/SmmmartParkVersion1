package com.smmmartparkversion1.db;

import com.smmmartparkversion1.model.User;

public interface IUserRepository {
    boolean createUser(User user);
    User findByUsername(String username);
    boolean updateUser(User user);
    boolean deleteUser(int userId);
}