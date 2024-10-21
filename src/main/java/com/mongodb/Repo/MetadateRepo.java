package com.mongodb.Repo;

import com.mongodb.Entity.Metadata;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MetadateRepo extends MongoRepository<Metadata , String> {

}
