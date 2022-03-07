package com.pillion.rider.controller;

import com.pillion.rider.model.User;
import com.pillion.rider.model.UserData;
import com.pillion.rider.service.UserService;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

  @Autowired
  private UserService userService;

  @GetMapping("/user")
  public List<User> getAll() {
    return userService.getUsers();
  }

  @GetMapping("/user/{email}")
  public User getByEmail(@PathVariable String email) {
    return userService.getByEmail(email);
  }

  @PostMapping("/user")
  public User saveUser(@RequestBody UserData userData) {
    return userService.saveUser(userData);
  }

}
