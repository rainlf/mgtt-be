package com.rainlf.mgttbe.service;

import com.rainlf.mgttbe.model.User;

import java.util.List;


public interface UserService {
    User login(String code);

    User findUserById(Integer id);

    List<User> save(List<User> users);

    User save(User user);

    User save(Integer userId, String userName);

    User save(Integer userId, String userName, byte[] avatar);

    int updateUserPoint(Integer userId, Integer points);

    List<User> getUserRank();

    List<User> findAllUsers();
}