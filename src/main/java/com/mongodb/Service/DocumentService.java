package com.mongodb.Service;

import com.mongodb.Advice.BaseApiExcepetions;
import com.mongodb.Dto.DocumentDto;
import com.mongodb.Dto.MetadataDto;
import com.mongodb.Entity.Directory;
import com.mongodb.Entity.Documents;
import com.mongodb.Entity.Metadata;
import com.mongodb.Mapper.DocumentMapper;
import com.mongodb.Repo.DocumentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;

import java.util.List;


@Service
public class DocumentService {
    @Autowired
    private DocumentMapper documentMapper;
    @Autowired
    private DocumentRepo documentRepo;
    @Autowired
    private DirectoryService directoryService;
    @Autowired
    private MetadataService metadataService;

    @Autowired
    private WorkSpaceService workSpaceService;

    public DocumentDto uploadfile(String directoryId , MultipartFile file) throws IOException {
        Directory directory = directoryService.getById(directoryId);
        if(directory == null)
            throw new BaseApiExcepetions(String.format("There is no Directory exist with this id [" + directoryId + "]"), HttpStatus.NOT_FOUND);
        if (file.isEmpty())
            throw new IOException("File is required and cannot be empty");
        String name = file.getOriginalFilename();
        Documents document = documentRepo.findByFileNameAndDirectoryId(name ,directoryId);
        if (document !=null)
            throw new RuntimeException("This file already exist in this directory ");
        Documents newDocument = new Documents(directoryId ,name , directory.getPath()+ File.separator + file.getOriginalFilename() );
        byte[] bytes = file.getBytes();
        Path path = Paths.get(directoryService.getPath(directoryId)+ File.separator + file.getOriginalFilename());
        boolean isUnique = isFileNameUnique(directoryService.getPath(directoryId) , name);
        if(!isUnique)
            throw new RuntimeException("There is a file already exist with this name ");
        Files.write(path, bytes);
        documentRepo.save(newDocument);
        metadataService.insertMetadata(newDocument.getId() , file.getContentType() ,file.getSize());
        return documentMapper.Map(newDocument);
    }

    public boolean isFileNameUnique(String directoryPath, String fileName) {
        File file = new File(directoryPath);
        if (!file.exists() || !file.isDirectory()) {
            throw new IllegalArgumentException("The specified path is not a valid path : " + directoryPath);
        }
        File[] files = file.listFiles();
        if (files != null)
            for (File f : files)
                if (f.getName().equalsIgnoreCase(fileName))
                    return false; // A file with the same name (ignoring case) exists
        return true;
    }
    public DocumentDto getByFileNameAndDirectoryId(String fileName , String directoryId ){
        Documents document = documentRepo.findByFileNameAndDirectoryId(fileName ,directoryId);
        if (document==null)
                throw new RuntimeException("No file exist with this name " +fileName+ " in this Directory");
        return documentMapper.Map(document);
    }
    public Documents getDocument(String id){
        Documents document = documentRepo.findById(id).orElse(null);
        if(document == null)
            throw new RuntimeException("No file exist with this id [" +id+ "] in this Directory" );
        if(document.getDeleted().matches("true"))
            throw new BaseApiExcepetions("This document has been deleted" , HttpStatus.NOT_FOUND);
        return document;
    }
    public DocumentDto getDocumentDto(String id){
        Documents document = documentRepo.findById(id).orElseThrow(()-> new RuntimeException("No file exist with " +
                "this id [" +id+ "] in this Directory"));
        return documentMapper.Map(document);
    }

    public DocumentDto updateDocument(String documentId , String newName){
        Documents document = getDocument(documentId);
        Directory directory = directoryService.getById(document.getDirectoryId());
        if (newName.matches(document.getFileName()))
            throw new BaseApiExcepetions("The new name you provided is the same old name", HttpStatus.CONFLICT);
        Documents document1 = documentRepo.findByFileNameAndDirectoryId(newName ,directory.getId());
        if (document1 !=null)
            throw new RuntimeException("There is file already exist with this new name");

        File oldDirectory = new File(getPath(documentId));
        System.out.println("the old path : " + getPath(documentId));
        File newDirectory = new File(oldDirectory.getParent() + File.separator + newName);
        System.out.println("the new path : " + oldDirectory.getParent() + File.separator + newName);
        boolean success = oldDirectory.renameTo(newDirectory);
        if (!success)
            throw new BaseApiExcepetions("This operation can not be done", HttpStatus.CONFLICT);

        document.setFileName(newName);
        String path = document.getPath();
        String newPath = path.substring(0 , path.lastIndexOf('\\')+1);
        document.setPath(newPath + newName);
        metadataService.increaseVersion(documentId);
        documentRepo.save(document);
        return documentMapper.Map(document);
    }

    public ResponseEntity<?> downloadDocument(String id) throws IOException {

        Path path = Paths.get(getPath(id));
        Resource resource = new UrlResource(path.toUri());

        if (!resource.exists() || !resource.isReadable()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        String contentType = Files.probeContentType(path);
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(org.springframework.http.MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    public void softDelete(String documentId){
        Documents document = getDocument(documentId);
        document.setDeleted("true");
        documentRepo.save(document);
    }

    public MetadataDto setTag(String documentId , String tag){
        return metadataService.setTag(documentId, tag);
    }
    public ResponseEntity<?> previewDocument(String id)throws IOException{

        String filePath = getPath(id);
        Path path = Paths.get(filePath);

        if (!Files.exists(path) || !Files.isReadable(path)) {
            return new ResponseEntity<>("File not found or not readable", HttpStatus.NOT_FOUND);
        }

        byte[] fileContent = Files.readAllBytes(path);
        // Encode the file content as Base64
        String base64EncodedContent =  Base64.getEncoder().encodeToString(fileContent);

        return new ResponseEntity<>(base64EncodedContent, HttpStatus.OK);
    }


    public Page<DocumentDto> sorting(String directoryId , String searchTerm , String pageNumber){
        directoryService.retrieveDirectoryById(directoryId);
        List<Documents> documentlist = documentRepo.findAllByDirectoryIdAndDeleted(directoryId , "false");
        List<Documents> documentlist1 = new ArrayList<>();
        List<Documents> documentlist2 = new ArrayList<>();
        for(Documents document : documentlist)
        {
            if(document.getFileName().contains(searchTerm)) {
                documentlist1.add(document);
            }else
                documentlist2.add(document);
        }

        documentlist.clear();
        for(Documents document : documentlist2)
        {
            Metadata metadata = metadataService.getMetadata(document.getId());
            if(metadata.getContentType().contains(searchTerm) || metadata.getTags().contains(searchTerm) )
                documentlist1.add(document);
            else
                documentlist.add(document);
        }
        documentlist1.addAll(documentlist);
        List<DocumentDto> documentDtos = documentsToDocumentsDto(documentlist1);

        Pageable pageable = PageRequest.of(Integer.parseInt(pageNumber), 7);
        int start = Math.min((int) pageable.getOffset(), documentDtos.size());
        int end = Math.min((start + pageable.getPageSize()), documentDtos.size());
        List<DocumentDto> pagedDocuments = documentDtos.subList(start, end);
        return new PageImpl<>(pagedDocuments, pageable, documentDtos.size());
    }

    public List<DocumentDto> documentsToDocumentsDto(List<Documents> documentsList) {
        List<DocumentDto> documentDtos = new ArrayList<>();
        for(Documents document : documentsList)
        {
            DocumentDto documentDto = documentMapper.Map(document);
            documentDtos.add(documentDto);
        }
        return documentDtos;
    }



    public List<DocumentDto> searchDocuments(String directoryId , String searchTerm ) {
        Directory directory = directoryService.getById(directoryId);
        if(directory == null)
            throw new BaseApiExcepetions(String.format("There is no Directory exist with this id [" + directoryId + "]"), HttpStatus.NOT_FOUND);
        List<Documents> documentlist = documentRepo.findAllByDirectoryIdAndDeleted(directoryId , "false");
        List<Documents> documentlist1 = new ArrayList<>();
        List<Documents> documentlist2 = new ArrayList<>();
        for(Documents document : documentlist)
        {
            if(document.getFileName().contains(searchTerm)) {
                documentlist1.add(document);
            }else
                documentlist2.add(document);
        }
        for(Documents document : documentlist2) {
            Metadata metadata = metadataService.getMetadata(document.getId());
            if (metadata.getContentType().contains(searchTerm) || metadata.getTags().contains(searchTerm))
                documentlist1.add(document);
        }
        return documentsToDocumentsDto(documentlist1);


    }

    public String getPath(String documentId){
        Documents document = getDocument(documentId);
        return workSpaceService.getFolderPath() + document.getPath();
    }

    public void deleteAllDocuments(String directoryId) {
        List<Documents> documentsList = documentRepo.findAllByDirectoryIdAndDeleted(directoryId , "false");
        for(Documents document : documentsList){
            document.setDeleted("true");
            documentRepo.save(document);
        }
    }
}
