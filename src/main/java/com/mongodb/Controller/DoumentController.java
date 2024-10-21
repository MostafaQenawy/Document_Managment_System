package com.mongodb.Controller;


import com.mongodb.Service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
@RestController
@RequestMapping("${mongo.document.base-uri}")
public class DoumentController {
    @Autowired
    private DocumentService documentService;

    @PostMapping("/{directoryId}")
    public ResponseEntity<?> uploadFile(@PathVariable String directoryId ,@RequestParam MultipartFile file) throws IOException {
        return ResponseEntity.ok(documentService.uploadfile(directoryId,file));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable String id){
        return ResponseEntity.ok(documentService.getDocumentDto(id));
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<?> downloadDocument(@PathVariable String id) throws IOException {
        return documentService.downloadDocument(id);
    }

    @GetMapping("/delete/{id}")
    public ResponseEntity<?> deleteDocument(@PathVariable String id) throws IOException {
        String jsonMessage = "{\"message\":\" [" + documentService.getDocument(id).getFileName() + "] Document has been deleted \"}";
        documentService.softDelete(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(jsonMessage, headers, HttpStatus.OK);
    }
    @GetMapping("/preview/{id}")
    public ResponseEntity<?> previewDocument(@PathVariable String id) throws IOException {
        return documentService.previewDocument(id);
    }

    @GetMapping("/list/{directoryId}")
    public ResponseEntity<?> listDocument(@PathVariable String directoryId ,@RequestParam  String searchTerm , @RequestParam String pageNumber)  {
        return ResponseEntity.ok(documentService.sorting( directoryId , searchTerm , pageNumber));
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<?> updateDocument(@PathVariable String id , @RequestParam String newName )  {
        return ResponseEntity.ok(documentService.updateDocument(id , newName));
    }

    @PostMapping("/tag/{id}")
    public ResponseEntity<?> setTag(@PathVariable String id , @RequestParam String tag ) {
        return ResponseEntity.ok(documentService.setTag(id , tag));
    }

    @GetMapping("/search/{directoryId}")
    public ResponseEntity<?> searchDocuments(@PathVariable String directoryId ,@RequestParam  String searchTerm ) {
        return ResponseEntity.ok(documentService.searchDocuments( directoryId , searchTerm));
    }


}
