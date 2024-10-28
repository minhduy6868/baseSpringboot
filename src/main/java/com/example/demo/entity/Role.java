package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.util.Set;

@Data //mạnh hơn getter và setter
@NoArgsConstructor
@AllArgsConstructor
@Builder // để build ra
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Role {
    @Id
    String name;
    String description;

//    @ManyToMany
//    Set<Permission> permissions;

}
