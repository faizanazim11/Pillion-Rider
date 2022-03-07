package com.pillion.rider.repository;

import java.util.Optional;

import com.pillion.rider.model.User;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String>{
    
    public Optional<User> findByEmail(String email);

    public Boolean existsByEmail(String email);

}
