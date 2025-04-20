package dev.remo.remo.Models.Request;

import java.util.List;
import java.util.Set;

import dev.remo.remo.Models.Users.User;

import lombok.Data;

@Data
public class SignUpRequest {
    

    private String email;

    private String password;

    private List<String> roles;


    public User convertToUser(){
        return new User("",this.email,this.password,this.roles);
    }
}
