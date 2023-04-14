package com.phunghv.base.controller;

import com.phunghv.base.apimodel.BaseRS;
import com.phunghv.base.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class UserController extends BaseController {


    @Autowired
    private UserService userService;

    @GetMapping("/users/{id}")
    public BaseRS detail(@PathVariable("id") Long id) {
        var user = userService.getUserDetail(id);
        return ok(user);
    }
}
