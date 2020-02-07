package com.association.admin.controller;

import com.association.user.Iface.RoleIface;
import component.BasicComponent;
import component.Proto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("role")
public class RoleController extends BasicComponent {
    @Autowired
    RoleIface roleIface;

    @PostMapping("roles")
    public Proto<?> roles() {
        return getResult(roleIface.getRoleTree());
    }
}
