package com.example.userservice.controller;


import com.example.userservice.service.UserService;
import com.example.userservice.vo.RequestUser;
import com.example.userservice.vo.ResponseUser;
import io.micrometer.core.annotation.Timed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
public class UserController {
    private Environment env;
    private UserService userService;
    @Autowired
    public UserController(Environment env, UserService userService) {
        this.env = env;
        this.userService = userService;
    }
    @GetMapping("/port")
    @Timed(value="users.port", longTask = true)
    public String status(){
        return String.format("It's Working in User Service"
                + ", port(local.server.port)=" + env.getProperty("local.server.port")
                + ", gateway ip=" + env.getProperty("gateway.ip")
                + ", token secret=" + env.getProperty("token.secret")
                + ", token expiration time=" + env.getProperty("token.expiration_time"));
    }
    @GetMapping("/welcome")
    @Timed(value="users.welcome", longTask = true)
    public String welcome(){
        return env.getProperty("greeting.message");
    }

    @PostMapping("/user")
    public ResponseEntity<RequestUser> createUser(@RequestBody RequestUser user){
        RequestUser responseUser = userService.createuser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseUser);
    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<ResponseUser> getUsers(@PathVariable("userId") String userId){


        return ResponseEntity.status(HttpStatus.CREATED).body(userService.getUserByUserId(userId));
    }

    @GetMapping("/users")
    public ResponseEntity<List<ResponseUser>> getUsers(){


        return ResponseEntity.status(HttpStatus.CREATED).body( userService.getUserByAll());
    }
}
