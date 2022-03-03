package com.pillion.rider.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserData {
    
    private String name;

    private String email;

    private String password;

    private String profileImage;

}
