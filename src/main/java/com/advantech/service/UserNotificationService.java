/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.model.UserNotification;
import com.advantech.repo.UserNotificationRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Wei.Cheng
 */
@Service
@Transactional
public class UserNotificationService {

    @Autowired
    private UserNotificationRepository repo;

    public List<UserNotification> findAll() {
        return repo.findAll();
    }

    public Page<UserNotification> findAll(Pageable pgbl) {
        return repo.findAll(pgbl);
    }

    public <S extends UserNotification> S save(S s) {
        return repo.save(s);
    }

    public Optional<UserNotification> findById(Integer id) {
        return repo.findById(id);
    }

    public void delete(UserNotification t) {
        repo.delete(t);
    }

    public void deleteAll() {
        repo.deleteAll();
    }

}
