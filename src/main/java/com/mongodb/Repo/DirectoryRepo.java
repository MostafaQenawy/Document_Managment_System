package com.mongodb.Repo;

import com.mongodb.Entity.Directory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DirectoryRepo extends MongoRepository<Directory , String> {
    Directory findByName(String name);
    List<Directory> findAllByWorkspaceId(String workspaceId);
    List<Directory> findAllByParentId(String parentId);


    Directory findByNameAndWorkspaceId(String name,String userId);

    Directory findByNameAndParentId(String name,String userId);

    Directory findByNameAndPath(String name ,String path);

}
