package com.mongodb.Service;

import com.mongodb.Advice.BaseApiExcepetions;
import com.mongodb.Dto.DirectoryDto;
import com.mongodb.Entity.Directory;
import com.mongodb.Entity.WorkSpace;
import com.mongodb.Mapper.DirectoryMapper;
import com.mongodb.Repo.DirectoryRepo;
import com.mongodb.Security.JwtTokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class DirectoryService {
    @Autowired
    private DirectoryRepo directoryRepo;

    @Autowired
    private WorkSpaceService workSpaceService;
    @Autowired
    private DirectoryMapper directoryMapper;

    @Autowired
    private DocumentService documentService;
    @Autowired
    private JwtTokenUtils tokenUtil;

    public DirectoryDto createDirectory(String id , String name) {

        Directory directory = null;
        Directory directory1;
        String parentPath;
        boolean parentIsWorkSpace;
        WorkSpace workSpace = workSpaceService.getWorkSpacebyID(id);
        if (workSpace == null) {
            directory = getById(id);
            if (directory == null)
                throw new BaseApiExcepetions(String.format("There is no WorkSpace exist with this user has the name ["
                        + name + "]"), HttpStatus.NOT_FOUND);
            parentIsWorkSpace = false;
            parentPath = getPath(id);

        }else {
            parentIsWorkSpace = true;
            parentPath = workSpaceService.getFolderPath() + workSpace.getPath();
        }
        String path =  parentPath + File.separator + name;
        boolean isUnique = isFileNameUnique(parentPath , name);
        File file = new File(path);
        if (file.exists() || !isUnique ) {
            throw new BaseApiExcepetions(String.format("There is Directory already exist with this user has the name ["
                    + name + "]"), HttpStatus.CONFLICT);
        }
        file.mkdirs();
        if (parentIsWorkSpace){
            directory1 = directoryRepo.save(new Directory(workSpace.getId(), null, name,
                    workSpace.getPath() + File.separator + name));
        }else
            directory1 = directoryRepo.save(new Directory(null, directory.getId(), name,
                    directory.getPath() + File.separator + name));
        return directoryMapper.Map(directory1);

    }
    public DirectoryDto retrieveDirectoryById(String id){
        Directory directory = getById(id);
        if(directory.getDeleted().matches("true"))
            throw new BaseApiExcepetions("This directory has been deleted" , HttpStatus.NOT_FOUND);
        return directoryMapper.Map(directory);
    }
    public List<DirectoryDto> retrieveAllDirectories(String workspaceIdOrParentId){
        if(!isValidId(workspaceIdOrParentId))
            throw new BaseApiExcepetions(String.format("There is no WorkSpace or Directory exist with this id [" + workspaceIdOrParentId + "]"), HttpStatus.NOT_FOUND);
        List<Directory> directoryList = directoryRepo.findAllByWorkspaceId(workspaceIdOrParentId);
        if (directoryList.isEmpty()) {
            System.out.println("This isn't workspace or this workspace is empty");
            directoryList = directoryRepo.findAllByParentId(workspaceIdOrParentId);
            if (directoryList.isEmpty())
                throw new BaseApiExcepetions(String.format("There are no Directories exist in This Directory "), HttpStatus.NOT_FOUND);
        }
        List<DirectoryDto> directoryDtos = new ArrayList<>();
        for(Directory directory : directoryList)
        {
            if(directory.getDeleted().matches("false")) {
                DirectoryDto directoryDto = directoryMapper.Map(directory);
                directoryDtos.add(directoryDto);
            }
        }
        return directoryDtos;
    }

    public DirectoryDto updateDirectory(String id ,String newName){
        if(!isValidId(id))
            throw new BaseApiExcepetions(String.format("There is no Workspace or Directory exist with this id ["
                    + id + "]"), HttpStatus.NOT_FOUND);
        Directory directory = getById(id);
        if (newName.matches(directory.getName()))
            throw new BaseApiExcepetions("The new name you provided is the same old name", HttpStatus.CONFLICT);
        if (isExist(directory.getWorkspaceId(),newName) || isExist(directory.getParentId() ,newName))
            throw new BaseApiExcepetions("There is Directory already exist with the name ["
                    + newName +"]", HttpStatus.CONFLICT);

        File oldDirectory = new File(getPath(id));
        File newDirectory = new File(oldDirectory.getParent() + File.separator + newName);
        boolean success = oldDirectory.renameTo(newDirectory);
        if (!success)
            throw new BaseApiExcepetions("This operation can not be done", HttpStatus.CONFLICT);

        directory.setName(newName);
        String path = directory.getPath();
        String newPath = path.substring(0 , path.lastIndexOf('\\')+1);
        directory.setPath(newPath + newName);
        directoryRepo.save(directory);
        updateNestedDirectories(id ,directory.getPath());
        return directoryMapper.Map(directory);
    }
    public Boolean isExist(String workspaceIdOrParentId,String name ){
        if(workspaceIdOrParentId== null)
            return false;
        Directory directory = directoryRepo.findByNameAndWorkspaceId(name , workspaceIdOrParentId);
        if (directory== null) {
            directory = directoryRepo.findByNameAndParentId(name, workspaceIdOrParentId);
            if(directory == null)
                return false;
        }
        return true;
    }
    public void updateNestedDirectories(String id ,String newPath){
        List<Directory> directoryList = getAllDirectory(id);
        for(Directory directory:directoryList)
        {
            String NewPath = newPath+File.separator+directory.getName();
            directory.setPath(NewPath);
            directoryRepo.save(directory);
            List<Directory> nestedDirectoryList = getAllDirectory(directory.getId());
            if(!nestedDirectoryList.isEmpty())
                updateNestedDirectories(directory.getId() , newPath);
        }
    }
    public boolean isValidId(String workspaceIdOrParentId){
        WorkSpace workSpace = workSpaceService.getWorkSpacebyID(workspaceIdOrParentId);
        if (workSpace == null) {
            Directory directory = getById(workspaceIdOrParentId);
            if (directory == null)
                return false;
        }
        return true;
    }

    public Directory getDirectoryByName(String name ){
        Directory directory = directoryRepo.findByName(name);
        if (directory== null)
            throw new BaseApiExcepetions(String.format("There is no directory exist in this workspace has the name ["
                    + name + "]"), HttpStatus.NOT_FOUND);
        return directory;
    }

    public boolean isFileNameUnique(String directoryPath, String fileName) {
        File directory = new File(directoryPath);
        if (!directory.exists() || !directory.isDirectory()) {
            throw new IllegalArgumentException("The specified path is not a valid directory: " + directoryPath);
        }
        File[] files = directory.listFiles();
        if (files != null)
            for (File file : files)
                if (file.getName().equalsIgnoreCase(fileName))
                    return false; // A file with the same name (ignoring case) exists
        return true;
    }

    public Directory getById(String id ){
        Directory directory = directoryRepo.findById(id).orElse(null);
        return directory;
    }
    public List<Directory> getAllDirectory(String workspaceIdOrParentId ){
        List<Directory> directoryList = directoryRepo.findAllByWorkspaceId(workspaceIdOrParentId);
        if (directoryList.isEmpty())
            directoryList = directoryRepo.findAllByParentId(workspaceIdOrParentId);
        return directoryList;
    }

    public void  deleteDirectory(String id){
        Directory directory = getById(id);
        if(directory == null)
            throw new BaseApiExcepetions(String.format("There is no Directory exist with this id ["
                    + id + "]"), HttpStatus.NOT_FOUND);
        File file = new File(getPath(id));
        if(!file.exists())
            throw new BaseApiExcepetions(String.format("There is no file exist with this id ["
                    + id + "]"), HttpStatus.NOT_FOUND);
        deleteNestedDirectories(id);
        directory.setDeleted("true");
        directoryRepo.save(directory);
    }
    public void deleteNestedDirectories(String id ){
        List<Directory> directoryList = getAllDirectory(id);
        for(Directory directory:directoryList)
        {
            List<Directory> nestedDirectoryList = getAllDirectory(directory.getId());
            if(!nestedDirectoryList.isEmpty())
                deleteNestedDirectories(directory.getId());
            documentService.deleteAllDocuments(directory.getId());
            directory.setDeleted("true");
            directoryRepo.save(directory);
        }
    }

    public String getPath(String directoryId){
        Directory directory = getById(directoryId);
        return workSpaceService.getFolderPath() + directory.getPath();
    }

}
