package com.mongodb.Controller;


import com.mongodb.Service.WorkSpaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${mongo.workspace.base-uri}")
public class WorkSpaceController {
    @Autowired
    private WorkSpaceService workSpaceService ;

    @PostMapping("${mongo.workspace.createWorkspace}")
    public ResponseEntity<?> createWorkSpace(@PathVariable String name) {
        return ResponseEntity.ok(workSpaceService.createWorkSpace(name));
    }

    @GetMapping("${mongo.workspace.getWorkspace}")
    public ResponseEntity<?> getWorkSpace(@PathVariable String name) {
        return ResponseEntity.ok(workSpaceService.retrieveWorkSpace(name));
    }
    @GetMapping("${mongo.workspace.getAll}")
    public ResponseEntity<?> getAllWorkSpaces() {
        return ResponseEntity.ok(workSpaceService.getAllWorkSpaces());
    }

    @PostMapping("${mongo.workspace.updateWorkspace}")
    public ResponseEntity<?> updateWorkSpace(@PathVariable String oldName , @PathVariable String newName) {
        return ResponseEntity.ok(workSpaceService.updateWorkSpace(oldName , newName));
    }
    @DeleteMapping("${mongo.workspace.deleteWorkspace}")
    public ResponseEntity<?> deleteWorkSpace(@PathVariable String name ) {
        workSpaceService.softdeleteWorkSpace(name);
        String jsonMessage = "{\"message\":\" [" + name + "] WorkSpace has been deleted \"}";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(jsonMessage, headers, HttpStatus.OK);
    }
}
