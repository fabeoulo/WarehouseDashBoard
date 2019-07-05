/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.controller;

import com.advantech.helper.SecurityPropertiesUtils;
import com.advantech.model.Floor;
import com.advantech.model.User;
import com.advantech.model.Warehouse;
import com.advantech.service.UserService;
import com.advantech.service.WarehouseService;
import static com.google.common.base.Preconditions.checkArgument;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Wei.Cheng
 */
@Controller
@RequestMapping("/WarehouseController")
public class WarehouseController extends CrudController<Warehouse> {

    @Autowired
    private WarehouseService warehouseService;

    @Autowired
    private UserService userService;
 
    @ResponseBody
    @RequestMapping(value = "findAll", method = {RequestMethod.GET})
    protected List<Warehouse> findAll(HttpServletRequest request) throws Exception {
        if (request.isUserInRole("ROLE_ADMIN")) {
            return warehouseService.findByFlag(0);
        } else {
            User user = SecurityPropertiesUtils.retrieveAndCheckUserInSession();
            Floor f = user.getFloor();
            checkArgument(f != null, "No storageSpace in setting, please try again.");
            return warehouseService.findByFloorAndFlag(f, 0);
        }
    }

    @Override
    @ResponseBody
    @RequestMapping(value = INSERT_URL, method = {RequestMethod.POST})
    protected ResponseEntity insert(@ModelAttribute Warehouse pojo, BindingResult bindingResult) throws Exception {
        User user = SecurityPropertiesUtils.retrieveAndCheckUserInSession();
        pojo.setFlag(0);
        warehouseService.save(pojo, user, "PUT_IN");
        return serverResponse(SUCCESS_MESSAGE);
    }

    @Override
    @ResponseBody
    @RequestMapping(value = UPDATE_URL, method = {RequestMethod.POST})
    protected ResponseEntity update(@ModelAttribute Warehouse pojo, BindingResult bindingResult) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    @ResponseBody
    @RequestMapping(value = DELETE_URL, method = {RequestMethod.POST})
    protected ResponseEntity delete(@RequestParam int id) throws Exception {
        User user = SecurityPropertiesUtils.retrieveAndCheckUserInSession();
        Warehouse pojo = warehouseService.findById(id).get();
        pojo.setFlag(1);
        warehouseService.save(pojo, user, "PULL_OUT");
        return serverResponse(SUCCESS_MESSAGE);
    }

}
