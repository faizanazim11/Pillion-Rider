package com.pillion.rider.model;

import java.util.HashMap;

import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "LoggedIn")
public class CurrentLoggedInUsers {
    
    private HashMap<String, String> loggedIn;

}
