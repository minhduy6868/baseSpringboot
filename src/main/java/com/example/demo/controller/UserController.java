package com.example.demo.controller;

import com.example.demo.dto.request.*;
import com.example.demo.entity.User;
import com.example.demo.exception.AppException;
import com.example.demo.exception.ErrorCode;
import com.example.demo.reponse.UserResponse;
import com.example.demo.service.UserService;
import com.nimbusds.jose.proc.SecurityContext;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/user")  //tạo endpoint chung cho cả lũ dưới
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    @Autowired
    UserService userService;

    @PostMapping("/create")   //endpoint
    ApiResponse<User> createUser(@RequestBody @Valid UserCreationRequest request) {
        ApiResponse apiResponse = new ApiResponse<>();
        apiResponse.setData(userService.createUser(request));
        return apiResponse;
    }

    @PostMapping("/createAdmin")
    ApiResponse<User> createAdmin(@RequestBody @Valid UserCreationRequest request) {
        ApiResponse apiResponse = new ApiResponse<>();
        apiResponse.setData(userService.createUserAmin(request));
        return apiResponse;
    }
    @PostMapping("/resetPassword")
    ApiResponse<User> resetPassword(@RequestBody @Valid UserResetPasswordResquest request) {
        ApiResponse apiResponse = new ApiResponse<>();
        apiResponse.setData(userService.resetPassword(request));
        return apiResponse;
    }

    @GetMapping("/getAllUsers")
    ApiResponse<List> getAllUsers() {

//        var authen = SecurityContextHolder.getContext().getAuthentication();  //lấy token lúc truyền vào ra để dưới check
//        //2 câu lệnh dưới là in ra
//        log.info("username: {}", authen.getName());
//        authen.getAuthorities().forEach(grantedAuthority -> log.info("grantedAuthority: ", grantedAuthority.getAuthority()));

        ApiResponse<List> apiResponse = new ApiResponse<>();
        apiResponse.setData(userService.getAllUsers());
        return apiResponse;
    }

    @GetMapping("/getUserById/{id}")
    User getUserById(@PathVariable String id) {  //truyền tham soos vào endpoint thì phải viết vaayj
        return userService.getUserById(id);
    }

    @PutMapping("/update/{id}")
    User updateUser(@PathVariable String id , @RequestBody UserUpdateRequest request) {
        return userService.updateUser(id, request);
    }

    @DeleteMapping("/delete/{id}")
    ApiResponse deleteUser(@PathVariable String id) {
        ApiResponse apiResponse = new ApiResponse<>();
        userService.deleteUser(id);
        apiResponse.setData("user has been deleted!");
        return apiResponse;
    }

    @PostMapping("/findbyUsername")
    ApiResponse<User> findbyUsername(@RequestBody AuthenticationRequest request) {
        ApiResponse apiResponse = new ApiResponse<>();
        apiResponse.setData(userService.findUserByUsername(request));
        return apiResponse;
    }

    @GetMapping("/getMyInfor")
    ApiResponse<UserResponse> getMyInfor() {
        ApiResponse apiResponse = new ApiResponse<>();
        apiResponse.setData(userService.getMyInfor());
        return apiResponse;
    }





}
