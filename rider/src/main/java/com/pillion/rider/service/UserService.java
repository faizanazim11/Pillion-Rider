package com.pillion.rider.service;

import com.pillion.rider.model.CustomUserDetails;
import com.pillion.rider.model.User;
import com.pillion.rider.model.UserData;
import com.pillion.rider.repository.UserRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
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

  public String processOAuthPostLogin(DefaultOidcUser oAuth2User) {
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
      return "ROLE_USER";
    }
    ouser.get().setLastLogin(new Date());
    UserDetails userDetails = loadUserByUsername(oAuth2User.getEmail());
    return "ROLE_USER";
  }

}
