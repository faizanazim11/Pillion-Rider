package com.pillion.rider.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.pillion.rider.model.Routes;

@Repository
public interface RoutesRepository extends MongoRepository<Routes, String>{
    
}
