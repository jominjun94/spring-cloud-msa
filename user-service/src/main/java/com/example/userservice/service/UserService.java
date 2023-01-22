package com.example.userservice.service;

import com.example.userservice.jpa.UserEntity;
import com.example.userservice.vo.RequestUser;
import com.example.userservice.vo.ResponseUser;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    RequestUser createuser(RequestUser user);

    ResponseUser getUserByUserId(String userId);
    List<ResponseUser> getUserByAll();

    RequestUser getUserDetailsByEmail(String username);
}
