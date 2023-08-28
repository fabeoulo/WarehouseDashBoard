/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * AND open the template in the editor.
 */
package com.advantech.repo;

import com.advantech.model.Floor;
import com.advantech.model.StorageSpaceGroup;
import com.advantech.model.Warehouse;
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
public interface WarehouseRepository extends JpaRepository<Warehouse, Integer> {

    /**
     * Flag 0: in warehouse wait for pull out Flag 1: Already pull out
     *
     * @param flag
     * @return
     */
    public List<Warehouse> findByFlag(int flag);

    @Query("SELECT w FROM Warehouse w JOIN w.storageSpace ss LEFT JOIN FETCH w.lineSchedule WHERE ss.id IN ?1 AND w.flag = ?2")
    public List<Warehouse> findBySsidsAndFlag(List<Integer> ids, int flag);

    @Query("SELECT w FROM Warehouse w JOIN w.storageSpace sp JOIN sp.storageSpaceGroup g WHERE g.floor = :floor AND w.flag = :flag")
    public List<Warehouse> findByFloorAndFlag(@Param("floor") Floor floor, @Param("flag") int flag);

    @Query("SELECT w FROM Warehouse w JOIN w.storageSpace sp JOIN sp.storageSpaceGroup g WHERE g.floor = ?2 AND w.po IN ?1 AND w.flag = ?3")
    public List<Warehouse> findByPosAndFloorAndFlag(List<String> pos, Floor floor, int flag);

    @Query("SELECT w FROM Warehouse w JOIN w.storageSpace sp JOIN sp.storageSpaceGroup g WHERE sp.storageSpaceGroup = :ssg AND w.flag = :flag")
    public List<Warehouse> findByStorageSpaceGroupAndFlag(@Param("ssg") StorageSpaceGroup storageSpaceGroup, @Param("flag") int flag);
}
