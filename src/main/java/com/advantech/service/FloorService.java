/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.model.Floor;
import com.advantech.repo.FloorRepository;
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
public class FloorService {

    @Autowired
    private FloorRepository repo;

    public List<Floor> findAll() {
        return repo.findAll();
    }

    public List<Floor> findByIdIn(List<Integer> ids) {
        return repo.findByIdIn(ids);
    }

    public Floor getOne(Integer id) {
        return repo.getOne(id);
    }

}
