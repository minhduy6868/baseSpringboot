package com.example.demo.service;

import com.example.demo.configuration.enums.Role;
import com.example.demo.dto.request.*;
import com.example.demo.entity.User;
import com.example.demo.exception.AppException;
import com.example.demo.exception.ErrorCode;
import com.example.demo.mapper.UserMapper;
import com.example.demo.reponse.UserResponse;
import com.example.demo.reponsitory.UserReponsitory;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;


@Service
@RequiredArgsConstructor

public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private UserReponsitory userReponsitory;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;


    public User createUser(UserCreationRequest request) {


    if(userReponsitory.existsByUsername(request.getUsername()))  //gọi hàm ra để check username đã tồn tại chưa
        throw new AppException(ErrorCode.USER_EXIST);
        HashSet<String> roles = new HashSet<>();
        roles.add(Role.USER.name());


       // User user = userMapper.toUser(request);
//Mapper giúp refactoer phần này nhanh chóng
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoles(roles);
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setDob(request.getDob());

        return userReponsitory.save(user);
    }

    public User createUserAmin(@Valid UserCreationRequest request) {
        if(userReponsitory.existsByUsername(request.getUsername()))  //gọi hàm ra để check username đã tồn tại chưa
            throw new AppException(ErrorCode.USER_EXIST);
        HashSet<String> roles = new HashSet<>();
        roles.add(Role.ADMIN.name());


        // User user = userMapper.toUser(request);
//Mapper giúp refactoer phần này nhanh chóng
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoles(roles);
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setDob(request.getDob());

        return userReponsitory.save(user);
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')") //set method chỉ cho role mình set
    public List<User> getAllUsers() {
        log.info("Kiểm tra role trước rồi mới vào method, khi không đúng role thì dòng này không được in ra ");
        return userReponsitory.findAll();
    }

    @PostAuthorize("hasAuthority('SCOPE_ADMIN')")
    public User findUserByUsername(AuthenticationRequest request) {
        log.info("Đã vào method findUserByUsername dù bất cứ role nào - vào thực hiện rồi mới check role sau");
        var user = userReponsitory.findByUsername(request.getUsername()).orElseThrow(() ->
                new AppException(ErrorCode.USER_NOT_FOUND));
        //print(user);
        return user;
    }

    @PostAuthorize("returnObject.username == authentication.name") //cho chạy phương thức trước lấy ra returnObject là username
    public User getUserById(String id) {                            // và so sánh với username của token, đúng thì return, ko thì lỗi
        return userReponsitory.findById(id).orElseThrow(           // vì vậy phải dùng PostAuthorrize
                () -> new AppException(ErrorCode.USER_NOT_FOUND)
        );
    }

    public UserResponse getMyInfor() {
        var context = SecurityContextHolder.getContext(); //lấy context từ token
        String username = context.getAuthentication().getName();  //trong context token lấy username

        var user = userReponsitory.findByUsername(username).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_FOUND)
        );
        log.info("Thông tin của User là: " + user);
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .dob(user.getDob())
                .roles(user.getRoles())
                .build();
    }

    public User updateUser(String id, UserUpdateRequest request) {
        User user = getUserById(id);
        userMapper.updateUser(user, request);

        return userReponsitory.save(user);
    }

    public void deleteUser(String id) {
        var userCheck = userReponsitory.findById(id);
        if(userCheck == null) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        } else userReponsitory.deleteById(id);
    }

    public Boolean resetPassword(UserResetPasswordResquest request) {
        var user = userReponsitory.findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        boolean check =  passwordEncoder.matches(request.getPassword(), user.getPassword()); // phương thức check pass nhập vào và pass tìm từ username

        if(check) {
            // Check if the new password is the same as the old password
//            if (passwordEncoder.matches(request.getNewpassword(), user.getPassword())) {
//                throw new AppException(ErrorCode.ERR_PASSWORD);
//            }

            // Set and encode the new password
            user.setPassword(passwordEncoder.encode(request.getNewpassword()));
            userReponsitory.save(user); // Don't forget to save the user entity
            return true;  // Wrap the response
        }
        return false;


    }


}
