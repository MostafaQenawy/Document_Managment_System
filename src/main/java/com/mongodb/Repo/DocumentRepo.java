package com.mongodb.Repo;

import com.mongodb.Entity.Documents;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentRepo extends MongoRepository<Documents, String> {
    Documents findByFileNameAndDirectoryId(String fileName, String directoryId);

    List<Documents> findAllByDirectoryIdAndDeleted(String directoryId, String deleted);
    //Page<Documents> findAllByDirectoryIdAndDeleted(String directoryId, String deleted);
}
