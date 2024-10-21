package com.mongodb.Service;

import com.mongodb.Advice.BaseApiExcepetions;
import com.mongodb.Dto.MetadataDto;
import com.mongodb.Entity.Documents;
import com.mongodb.Entity.Metadata;
import com.mongodb.Mapper.MetadataMapper;
import com.mongodb.Repo.MetadateRepo;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import javax.print.Doc;

@Service
public class MetadataService {

    @Autowired
    private MetadateRepo metadateRepo;
    @Autowired
    private DocumentService documentService;
    @Autowired
    private MetadataMapper metadataMapper;

    public MetadataDto insertMetadata(String id, String contentType , Long size){

        Metadata metadata = new Metadata(id,contentType , size);
        metadata = metadateRepo.save(metadata);
        return metadataMapper.Map(metadata);
    }
    public MetadataDto setTag(String documentId, String tag){

        Metadata metadata = getMetadata(documentId);
        Documents documents = documentService.getDocument(metadata.getId());
        metadata.getTags().add(tag);
        metadateRepo.save(metadata);
        return metadataMapper.Map(metadata);
    }

    public MetadataDto increaseVersion(String id){

        Metadata metadata = getMetadata(id);
        metadata.setVersioning_info(metadata.getVersioning_info()+1);
        metadateRepo.save(metadata);
        return metadataMapper.Map(metadata);
    }

    public Metadata getMetadata(String id){
        return metadateRepo.findById(id).orElseThrow(()-> new RuntimeException("Metadata Not found for this file")) ;
    }


}
