package com.mongodb.Controller;

import com.mongodb.Entity.Directory;
import com.mongodb.Service.DirectoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${mongo.directory.base-uri}")
public class DirectoryController {
    @Autowired
    private DirectoryService directoryService ;

    @PostMapping("/{id}/{name}")
    public ResponseEntity<?> createDirectory(@PathVariable String id  , @PathVariable String name) {
        return ResponseEntity.ok(directoryService.createDirectory(id ,name));
    }

    @PutMapping("/{id}/{newName}")
    public ResponseEntity<?> updateDirectory(@PathVariable String id  , @PathVariable String newName) {
        return ResponseEntity.ok(directoryService.updateDirectory(id ,newName));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDirectory(@PathVariable String id) {
        String jsonMessage = "{\"message\":\" [" + directoryService.getById(id).getName() + "] Directory has been deleted \"}";
        directoryService.deleteDirectory(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(jsonMessage, headers, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDirectory(@PathVariable String id ) {
        return ResponseEntity.ok(directoryService.retrieveDirectoryById(id));
    }

    @GetMapping("/All/{id}")
    public ResponseEntity<?> getAllDirectories(@PathVariable String id ) {
        return ResponseEntity.ok(directoryService.retrieveAllDirectories(id));
    }


}
