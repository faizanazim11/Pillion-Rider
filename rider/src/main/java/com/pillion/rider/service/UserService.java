package com.pillion.rider.service;

import com.pillion.rider.model.CurrentLoggedInUsers;
import com.pillion.rider.model.CustomUserDetails;
import com.pillion.rider.model.User;
import com.pillion.rider.model.UserData;
import com.pillion.rider.repository.CurrentLoggedInUsersRepository;
import com.pillion.rider.repository.UserRepository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private CurrentLoggedInUsersRepository currentLoggedInUsersRepository;

  public List<User> getUsers() {
    return userRepository.findAll();
  }

  public User getByEmail(String email) {
    return userRepository.findByEmail(email).orElse(null);
  }

  public User saveUser(UserData userData) {
    return userRepository.save(new User(userData));
  }

  public Boolean existsByEmail(String email)
  {
    return userRepository.existsByEmail(email);
  }

  @Override
  public UserDetails loadUserByUsername(String email)
    throws UsernameNotFoundException {
    Optional<User> user = userRepository.findByEmail(email);
    user.orElseThrow(() -> new UsernameNotFoundException("Invalid Email!!"));
    return user.map(CustomUserDetails::new).get();
  }

  public void processOAuthPostLogin(DefaultOidcUser oAuth2User) {
    Optional<User> ouser = userRepository.findByEmail(oAuth2User.getEmail());
    if (!ouser.isPresent()) {
      User user = new User();
      user.setName(oAuth2User.getFullName());
      user.setEmail(oAuth2User.getEmail());
      user.setPassword("");
      user.setRole("ROLE_USER");
      user.setProfileImage(oAuth2User.getPicture());
      user.setLastLogin(new Date());
      userRepository.save(user);
      System.out.println("Save OAuth!");
      return;
    }
    User user =  ouser.get();
    user.setLastLogin(new Date());
    userRepository.save(user);
  }

  public void addLoggedIn(String token, String email){
    token = token.replace('.', ' ');
    if(currentLoggedInUsersRepository.findAll().isEmpty())
    {
      currentLoggedInUsersRepository.save(new CurrentLoggedInUsers(new HashMap<>()));
    }
    CurrentLoggedInUsers currentLoggedInUsers = currentLoggedInUsersRepository.findAll().get(0);
    if(currentLoggedInUsers.getLoggedIn().isEmpty())
    {
      currentLoggedInUsers.getLoggedIn().put(token, email);
      System.out.println(token);
      System.out.println(email);
      currentLoggedInUsersRepository.deleteAll();
      currentLoggedInUsersRepository.save(currentLoggedInUsers);
      return;
    }
    if(currentLoggedInUsers.getLoggedIn().containsValue(email))
    {
      String gotToken = null;
      for (Entry<String, String> mapp : currentLoggedInUsers.getLoggedIn().entrySet()) {
        if(mapp.getValue().equals(email))
        {
          gotToken = mapp.getKey();
          break;
        }
      }
      currentLoggedInUsers.getLoggedIn().remove(gotToken);
    }
    currentLoggedInUsers.getLoggedIn().put(token, email);
    System.out.println(token);
    System.out.println(email);
    currentLoggedInUsersRepository.deleteAll();
    currentLoggedInUsersRepository.save(currentLoggedInUsers);
  }

}
