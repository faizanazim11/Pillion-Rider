package com.pillion.rider.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.pillion.rider.model.Stops;

@Repository
public interface StopsRepository extends MongoRepository<Stops, String>{
    
}
