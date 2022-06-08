package com.pillion.rider.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.pillion.rider.model.GraphMatrix;

@Repository
public interface GraphMatrixRepository extends MongoRepository<GraphMatrix, String>{
    
    public Optional<GraphMatrix> findByGraphType(String graphType);

}
