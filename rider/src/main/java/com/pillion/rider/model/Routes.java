package com.pillion.rider.model;

import java.util.LinkedList;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "Routes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Routes {
    
    @Id
    private String email;

    private LinkedList<String> locations;

    private LinkedList<Integer> distances;
    
}
