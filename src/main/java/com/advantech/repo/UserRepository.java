/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.repo;

import com.advantech.model.User;
import com.advantech.model.UserNotification;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    public User findByJobnumber(String jobnumber);
    
    public List<User> findByUserNotifications(UserNotification notifi);
    
    @Procedure("usp_QuickInsertUserWithName")
    public void saveUserWithNameByProc(String jobnumber, String email, String name);
}
