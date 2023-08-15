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

        @Query("SELECT s FROM StorageSpace s JOIN s.storageSpaceGroup sg "
            + "WHERE sg.floor = :floor AND sg.enabled = 1 "
            + "ORDER BY sg.priority, s.priority")
    public List<StorageSpace> findByFloor(@Param("floor") Floor f);

    @Query("SELECT s FROM StorageSpace s JOIN s.storageSpaceGroup sg "
            + "WHERE sg.floor IN ?1 AND sg.enabled = 1 AND s.blocked = FALSE "
            + "ORDER BY sg.floor.id, sg.priority, s.priority")
    public List<StorageSpace> findEmptyByFloors(List<Floor> f);
//    public List<StorageSpace> findByStorageSpaceGroupFloorInAndBlockedFalseOrderByStorageSpaceGroupPriorityAscPriorityAsc(List<Floor> f);

    @Query("SELECT s FROM StorageSpace s JOIN s.storageSpaceGroup sg "
            + "WHERE s.id IN ?1 AND sg.enabled = 1 "
            + "ORDER BY sg.priority, s.priority")
    public List<StorageSpace> findAllByIdOrdered(List<Integer> id);

//    @Query("SELECT s FROM StorageSpace s JOIN FETCH s.storageSpaceGroup sg JOIN FETCH sg.floor f WHERE s.id = :id")
//    public StorageSpace findWithLazyById(@Param("id") Integer id);
    public List<StorageSpace> findByStorageSpaceGroupOrderByName(StorageSpaceGroup group);

}
