package com.example.demo.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Locale;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class UserUpdateRequest {
     String username;
     String password;
     String firstName;
     String lastName;
     Locale dob;

}
