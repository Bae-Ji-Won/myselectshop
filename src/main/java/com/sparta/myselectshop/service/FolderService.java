package com.sparta.myselectshop.service;

import com.sparta.myselectshop.dto.FolderResponseDto;
import com.sparta.myselectshop.entity.Folder;
import com.sparta.myselectshop.entity.User;
import com.sparta.myselectshop.repository.FolderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FolderService {

    private final FolderRepository folderRepository;

    public void addFolders(List<String> folderNames, User user) {
        // 기존 폴더 리스트 찾기
        List<Folder> existFolderList = folderRepository.findAllByUserAndNameIn(user,folderNames);
        
        List<Folder> folderList = new ArrayList<>();
        
        // 기존 폴더들의 이름과 내가 받아온 폴더의 이름이 중복이 되는지 확인
        for (String folderName : folderNames) {
            if(!isExistFolderName(folderName, existFolderList)){
                // 중복이 되지 않은 폴더라면 해당 폴더 객체를 생성
                Folder folder = new Folder(folderName,user);
                // 폴더 리스트에 저장시켜줍니다.
                folderList.add(folder);
            }else{      // 폴더명이 중복일 경우
                throw new IllegalArgumentException("폴더명이 중복되었씁니다.");
            }
        }

        folderRepository.saveAll(folderList);
    }

    // 저장된 폴더 전부 가져오기
    public List<FolderResponseDto> getFolders(User user) {
        List<Folder> folderList = folderRepository.findAllByUser(user);
        List<FolderResponseDto> responseDtoList = new ArrayList<>();

        for (Folder folder : folderList) {
            responseDtoList.add(new FolderResponseDto(folder));
        }

        return responseDtoList;
    }
    
    // 기존 폴더 리스트와 새로 받은 폴더 이름이 같은지 비교
    private boolean isExistFolderName(String folderName, List<Folder> existFolderList) {
        for (Folder existFolder : existFolderList) {
            if(folderName.equals(existFolder.getName())){
                return true;
            }
        }
        return false;
    }


}
