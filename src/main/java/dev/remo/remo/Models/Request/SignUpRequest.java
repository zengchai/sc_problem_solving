package dev.remo.remo.Models.Request;

import java.util.List;
import java.util.Set;

import dev.remo.remo.Models.Users.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignUpRequest {
    
    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min=8)
    private String password;

    private List<String> roles;


    public User convertToUser(){
        return new User(this.email,this.password,this.roles);
    }
}
