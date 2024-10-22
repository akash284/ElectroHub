package com.lcwa.electronic.store.dtos;


import com.lcwa.electronic.store.validate.ImageNameValid;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto
{

    private String userId;

    @Size(min=3,max=25,message="Invalid name")
    private String name;

  //  @Email(message="email is invalid")
    @Pattern(regexp = "^[a-z0-9][-a-z0-9._]+@([-a-z0-9]+\\.)+[a-z]{2,5}$",message="Invalid user email")
    @NotBlank(message="email is required")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    @Size(min=4,max=6,message="invalid gender")
    private String gender;

    @NotBlank(message = "Write something about yourself !")
    private String about;

// user k sath uska role bhi bhejna heto islie
    private List<RoleDto> roles;
//    // @Pattern

    //  Custom validator
    @ImageNameValid
    private String imageName;
}
