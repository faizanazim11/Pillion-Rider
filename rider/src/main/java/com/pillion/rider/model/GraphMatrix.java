package com.pillion.rider.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "graphs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GraphMatrix {

    @Id
    private String id;

    private Integer graph[][]; 
    
    private String graphType;

}
