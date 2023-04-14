package com.phunghv.base.service;

import com.phunghv.base.persistent.domain.entity.User;
import com.phunghv.base.persistent.domain.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserService {

    @Autowired
    private UserRepo userRepo;

    public User getUserDetail(Long userId) {
        return userRepo.findById(userId).orElse(null);
    }
}
