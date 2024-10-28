package com.example.demo.dto.request;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Locale;

@Data //mạnh hơn getter và setter
@NoArgsConstructor
@AllArgsConstructor
@Builder // để build ra
@FieldDefaults(level = AccessLevel.PRIVATE) //mặc định private giúp code clear hơn
public class UserCreationRequest {
     String username;

    @Size(min = 8, message = "INVALID_PASSWORD")
     String password;
     String firstName;
     String lastName;
     Locale dob;

}
