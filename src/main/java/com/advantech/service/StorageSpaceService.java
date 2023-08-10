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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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
    public List<StorageSpace> findAllByIdOrdered(List<Integer> id) {
        return id.isEmpty() ? new ArrayList<>() : repo.findAllByIdOrdered(id);
    }

    public List<StorageSpace> findAllById(List<Integer> id) {
        return repo.findAllById(id);
    }

    public List<StorageSpace> findByFloor(Floor f) {
        return repo.findByFloor(f);
    }

    public List<StorageSpace> findEmptyByFloor(Floor f) {
        return repo.findEmptyByFloor(f);
    }

    public StorageSpace findFirstEmptyByFloorAndPriority(Floor f, int ssgId) {
        List<StorageSpace> lss = this.findEmptyByFloor(f);
        Optional<StorageSpace> ss = lss.stream().filter(l -> l.getStorageSpaceGroup().getId() == ssgId).findFirst();
        if (ss.isPresent()) {
            return ss.get();
        } else {
            return lss.isEmpty() ? null : lss.get(0);
        }
    }

    public List<StorageSpace> findByStorageSpaceGroupOrderByName(StorageSpaceGroup group) {
        return repo.findByStorageSpaceGroupOrderByName(group);
    }

    public <S extends StorageSpace> S save(S s) {
        return repo.save(s);
    }
    
    public <S extends StorageSpace> Iterable<S> saveAll(Iterable<S> s) {
        return repo.saveAll(s);
    }
}
