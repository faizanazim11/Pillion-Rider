package com.pillion.rider.model;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StopsDetails {
    
    private String streetName;

    private ArrayList<Integer> coords;
    
}
