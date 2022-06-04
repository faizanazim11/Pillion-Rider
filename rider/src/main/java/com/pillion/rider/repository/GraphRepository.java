package com.pillion.rider.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.pillion.rider.model.Graph;

@Repository
public interface GraphRepository extends MongoRepository<Graph, String>{
    
    public Optional<Graph> findByGraphType(String graphType);

}
