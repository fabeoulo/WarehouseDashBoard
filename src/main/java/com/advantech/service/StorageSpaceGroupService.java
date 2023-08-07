/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.model.Floor;
import com.advantech.model.StorageSpaceGroup;
import com.advantech.repo.StorageSpaceGroupRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Wei.Cheng
 */
@Service
@Transactional
public class StorageSpaceGroupService {

    @Autowired
    private StorageSpaceGroupRepository repo;

    public StorageSpaceGroup getOne(Integer id) {
        return repo.getOne(id);
    }

    public List<StorageSpaceGroup> findAllByOrderByName() {
        return repo.findAllByOrderByName();
    }

    public List<StorageSpaceGroup> findByFloorOrderByName(Floor floor) {
        return repo.findByFloorAndEnabledOrderByPriority(floor, 1);
    }

}
