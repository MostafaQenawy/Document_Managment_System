package com.mongodb.Repo;

import com.mongodb.Entity.WorkSpace;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkSpaceRepo extends MongoRepository<WorkSpace , String> {
    @Override
    Optional<WorkSpace> findById(String id);

    WorkSpace findByName(String name);
    WorkSpace findByNameAndUserId(String name, Long userId);

    List<WorkSpace> findAllByUserId(Long userId);

    void delete(WorkSpace workSpace);

}
