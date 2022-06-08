package com.pillion.rider.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.pillion.rider.model.GraphMapper;

@Repository
public interface GraphMapperRepository extends MongoRepository<GraphMapper, String>{
    
}
