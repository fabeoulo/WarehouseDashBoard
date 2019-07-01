/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.model.LineScheduleStatus;
import com.advantech.repo.LineScheduleStatusRepository;
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
public class LineScheduleStatusService {

    @Autowired
    private LineScheduleStatusRepository repo;

    public List<LineScheduleStatus> findAll() {
        return repo.findAll();
    }

    public LineScheduleStatus getOne(Integer id) {
        return repo.getOne(id);
    }

}
