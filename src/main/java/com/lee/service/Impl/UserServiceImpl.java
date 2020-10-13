package com.lee.service.Impl;

import com.lee.dao.UserDao;
import com.lee.pojo.User;
import com.lee.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public User checkUser(String username) {
        User user = userDao.queryByUsernameAndPassword(username);
        return user;
    }
}
