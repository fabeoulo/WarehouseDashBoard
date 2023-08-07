/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.model.Floor;
import com.advantech.model.StorageSpace;
import com.advantech.model.StorageSpaceGroup;
import com.advantech.repo.StorageSpaceRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Wei.Cheng
 */
@Service
@Transactional
public class StorageSpaceService {

    @Autowired
    private StorageSpaceRepository repo;

    public List<StorageSpace> findAll() {
        return repo.findAll();
    }

    public Optional<StorageSpace> findById(Integer id) {
        return repo.findById(id);
    }

//    public StorageSpace findWithLazyById(Integer id) {
//        return repo.findWithLazyById(id);
//    }
    
    public List<StorageSpace> findAllById(List<Integer> id) {
        return repo.findAllById(id);
    }

    public List<StorageSpace> findByFloor(Floor f) {
        return repo.findByFloor(f);
    }

    public List<StorageSpace> findEmptyByFloor(Floor f) {
        return repo.findEmptyByFloor(f);
    }

    public List<StorageSpace> findByStorageSpaceGroupOrderByName(StorageSpaceGroup group) {
        return repo.findByStorageSpaceGroupOrderByName(group);
    }

    public <S extends StorageSpace> S save(S s) {
        return repo.save(s);
    }
}
