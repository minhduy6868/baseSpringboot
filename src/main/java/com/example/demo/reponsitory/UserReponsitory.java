package com.example.demo.reponsitory;

import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserReponsitory extends JpaRepository<User, String> {
    boolean existsByUsername(String username); //khoogn cho username trùng nhau
    //boolean existsByEmail(String email);    tương tự với email
    Optional<User> findByUsername(String username);

}
