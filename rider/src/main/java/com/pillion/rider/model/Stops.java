package com.pillion.rider.model;

import java.util.ArrayList;
import java.util.TreeMap;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "Stops")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Stops {
    
    @Id
    private String id;

    private TreeMap<String, ArrayList<Integer>> stops;

}
