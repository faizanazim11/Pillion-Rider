package com.pillion.rider.model;

import java.sql.Date;
import java.util.HashMap;
import java.util.LinkedList;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "User")
public class User {
    
    @Id
    private String id;
    
    private String name;
    
    private String email;
    
    private String password;
    
    private String profileImage;
    
    private LinkedList<String> friends;
    
    private HashMap<String, Double> lastLocation;

    private Date lastLogin;

    public User(UserData userData)
    {
        this.name = userData.getName();
        this.email = userData.getEmail();
        this.password = userData.getPassword();
        this.profileImage = userData.getProfileImage();
        this.friends = null;
        this.lastLocation = null;
        this.lastLogin = null;   
    }

}
