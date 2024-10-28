package com.example.demo.reponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;
import java.util.Locale;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    String id;
    String username;
    String firstName;
    String lastName;
    Locale dob;
    Set<String> roles;
}
