package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    String id;

    String username;
    String password;
    String firstName;
    String lastName;
    Locale dob;

    @ElementCollection // Thêm annotation này
    Set<String> roles = new HashSet<>(); // Khởi tạo Set

}
