package com.example.demo.reponse;

import com.example.demo.entity.User;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class AuthenticationResponse {
    boolean authenticated;
    User userInfor;
    String token;
}
