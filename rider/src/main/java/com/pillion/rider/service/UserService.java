package com.pillion.rider.service;

import java.util.List;

import com.pillion.rider.model.User;
import com.pillion.rider.model.UserData;
import com.pillion.rider.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public User getByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User saveUser(UserData userData)
    {
        return userRepository.save(new User(userData));
    }

}
