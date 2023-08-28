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
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;
import org.hibernate.Hibernate;
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
        return repo.findByFloors(Arrays.asList(f));
    }

    public List<StorageSpace> findWhActiveByFloors(List<Floor> f) {
        List<StorageSpace> l = repo.findByFloors(f);
        return l;
    }
    
    public StorageSpace findFirstEmptyByFloorAndSsg(Floor f, int ssgId) {
        List<StorageSpace> lss = repo.findEmptyByFloors(Arrays.asList(f));
        Optional<StorageSpace> ss = lss.stream().filter(l -> l.getStorageSpaceGroup().getId() == ssgId).findFirst();
        if (ss.isPresent()) {
            return ss.get();
        } else {
            return lss.isEmpty() ? null : lss.get(0);
        }
    }

    public Map<String, List<StorageSpace>> findEmptyMapByFloors(List<Floor> f) {
        List<StorageSpace> l = repo.findEmptyByFloors(f);
        // key of map is random order
        Map<String, List<StorageSpace>> m = l.stream().collect(Collectors.groupingBy(
                ss -> ss.getStorageSpaceGroup().getFloor().getName()
        ));
        return new TreeMap<>(m);
//        return l.stream().collect(Collectors.groupingBy(
//                ss -> ss.getStorageSpaceGroup().getFloor().getName()
//        ));
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
