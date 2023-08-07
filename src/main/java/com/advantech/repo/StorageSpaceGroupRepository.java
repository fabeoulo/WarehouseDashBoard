/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.repo;

import com.advantech.model.Floor;
import com.advantech.model.StorageSpaceGroup;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng
 */
@Repository
public interface StorageSpaceGroupRepository extends JpaRepository<StorageSpaceGroup, Integer> {

    public List<StorageSpaceGroup> findAllByOrderByName();
    
    public List<StorageSpaceGroup> findByFloorAndEnabledOrderByPriority(Floor floor, int enabledFlag);
}
