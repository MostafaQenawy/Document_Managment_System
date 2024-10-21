package com.mongodb.Service;

import com.mongodb.Advice.BaseApiExcepetions;
import com.mongodb.Dto.WorkSpaceDto;
import com.mongodb.Entity.Directory;
import com.mongodb.Entity.WorkSpace;
import com.mongodb.Mapper.WorkSpaceMapper;
import com.mongodb.Repo.WorkSpaceRepo;
import com.mongodb.Security.JwtTokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class WorkSpaceService {
    @Value("${storage.local}")
    private String folderPath;

    @Autowired
    private WorkSpaceRepo workSpaceRepo;

    @Autowired
    private WorkSpaceMapper workSpaceMapper;

    @Autowired
    private DirectoryService directoryService;

    @Autowired
    private JwtTokenUtils tokenUtil;

    public WorkSpaceDto createWorkSpace(String name){
        if (getUserId() == null)
            throw new RuntimeException("Username is Null");
        String path = folderPath+File.separator+getUserId();
        File mainDirectory = new File(path);
        if (!mainDirectory.exists()){
            mainDirectory.mkdirs();
        }
        WorkSpace workSpace= workSpaceRepo.findByNameAndUserId(name, getUserId());
        File directory = new File(path+File.separator+name);
        if (directory.exists() || workSpace != null){
            throw new BaseApiExcepetions(String.format("There is WorkSpace already exist with this user has the name ["
                    + name+ "]") , HttpStatus.CONFLICT);
        }
        directory.mkdirs();
        WorkSpace workSpace1 = workSpaceRepo.save(
                new WorkSpace(getUserId() , name ,File.separator+getUserId()+File.separator+name));
        return workSpaceMapper.Map(workSpace1);
    }

    public WorkSpace getWorkSpacebyID(String id){
        return workSpaceRepo.findById(id).orElse(null);
    }
    public WorkSpace getWorkSpace(String name){
        Long userId = getUserId();
        WorkSpace workSpace= workSpaceRepo.findByNameAndUserId(name,userId);
        if(userId == null || workSpace == null)
            throw new BaseApiExcepetions(String.format("There is no WorkSpace exist with this user has the name ["
                    + name+ "]"), HttpStatus.NOT_FOUND);
        if(workSpace.getDeleted().matches("true"))
            throw new BaseApiExcepetions(String.format("This workspace has been deleted "), HttpStatus.NOT_FOUND);
        return workSpace;
    }

    public WorkSpaceDto retrieveWorkSpace(String name){
        WorkSpace workSpace = getWorkSpace(name);
        if(workSpace.getDeleted().matches("true"))
            throw new BaseApiExcepetions(String.format("This workspace has been deleted "), HttpStatus.NOT_FOUND);
        return workSpaceMapper.Map(workSpace);
    }

    public Boolean isExist(String name){
        Long userId = getUserId();
        WorkSpace workSpace = workSpaceRepo.findByNameAndUserId(name,userId);
        if(userId == null || workSpace == null)
            return false;
        return true;
    }
    public List<WorkSpaceDto> getAllWorkSpaces(){
        Long userId = getUserId();
        List<WorkSpace> workSpaces = workSpaceRepo.findAllByUserId(userId);
        List<WorkSpaceDto> workSpaceDtos = new ArrayList<>();
        for(WorkSpace workSpace:workSpaces)
        {
            if (workSpace.getDeleted().matches("false")) {
                WorkSpaceDto workSpaceDto = workSpaceMapper.Map(workSpace);
                workSpaceDtos.add(workSpaceDto);
            }
        }
        if(userId == null || workSpaces == null || workSpaceDtos == null)
            throw new BaseApiExcepetions(String.format("There is no WorkSpace exist with this user has this email["
                    + getEmail()+ "]"), HttpStatus.NOT_FOUND);
        return workSpaceDtos;
    }


    public WorkSpaceDto updateWorkSpace(String oldName , String newName) {
        if (oldName.matches(newName))
            throw new BaseApiExcepetions("The new name you provided is the same old name", HttpStatus.CONFLICT);
        if (isExist(newName))
            throw new BaseApiExcepetions("There is workspace already exist with the name [" + newName +"]"
                    , HttpStatus.CONFLICT);
        String path = folderPath + File.separator + getUserId();
        File oldDirectory = new File(path + File.separator + oldName);
        File newDirectory = new File(path + File.separator + newName);
        boolean success = oldDirectory.renameTo(newDirectory);
        if (!success)
            throw new BaseApiExcepetions("This operation can not be done", HttpStatus.CONFLICT);

        WorkSpace workSpace = getWorkSpace(oldName);
        workSpace.setName(newName);
        workSpace.setPath( File.separator + getUserId() + File.separator + newName);
        workSpaceRepo.save(workSpace);
        return workSpaceMapper.Map(workSpace);
    }

    public void softdeleteWorkSpace(String name){
        String path = folderPath + File.separator + getUserId();
        File directory = new File(path + File.separator + name);
        if (!directory.exists() || !isExist(name))
            throw new BaseApiExcepetions("There is no workspace exist with this user has the name ["
                    + name +"]", HttpStatus.CONFLICT);

        WorkSpace workSpace = getWorkSpace(name);
        List<Directory> directoryList = directoryService.getAllDirectory(workSpace.getId());
        for(Directory directory1 : directoryList){
            directoryService.deleteDirectory(directory1.getId());
        }
        workSpace.setDeleted("true");
        workSpaceRepo.save(workSpace);
    }

    public Long getUserId(){
        String jwtToken = tokenUtil.getJwtToken();
        return tokenUtil.getUserIdFromToken(jwtToken);
    }

    public String getEmail(){
        String jwtToken = tokenUtil.getJwtToken();
        return tokenUtil.getEmailFromToken(jwtToken);
    }


    public String getFolderPath(){
        return folderPath ;
    }
}
