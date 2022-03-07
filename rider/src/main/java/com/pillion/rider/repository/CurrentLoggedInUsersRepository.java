package com.pillion.rider.repository;

import com.pillion.rider.model.CurrentLoggedInUsers;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrentLoggedInUsersRepository extends MongoRepository<CurrentLoggedInUsers, String>{
    
}
