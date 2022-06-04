package com.pillion.rider.model;

import java.util.LinkedList;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRoute {

    private LinkedList<String> locations;

    private LinkedList<Integer> distances;
    
}
