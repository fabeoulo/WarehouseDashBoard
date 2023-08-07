/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.repo;

import com.advantech.model.Floor;
import com.advantech.model.StorageSpace;
import com.advantech.model.StorageSpaceGroup;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng
 */
@Repository
public interface StorageSpaceRepository extends JpaRepository<StorageSpace, Integer> {

    @Query("select s from StorageSpace s join s.storageSpaceGroup sg where sg.floor = :floor order by sg.priority")
    public List<StorageSpace> findByFloor(@Param("floor") Floor f);

    @Query("select s from StorageSpace s join s.storageSpaceGroup sg where sg.floor = :floor and s.blocked = FALSE order by sg.priority, s.priority")
    public List<StorageSpace> findEmptyByFloor(@Param("floor") Floor f);    
    
//    @Query("SELECT s FROM StorageSpace s JOIN FETCH s.storageSpaceGroup sg JOIN FETCH sg.floor f where s.id = :id")
//    public StorageSpace findWithLazyById(@Param("id") Integer id);
    
    public List<StorageSpace> findByStorageSpaceGroupOrderByName(StorageSpaceGroup group);

}
