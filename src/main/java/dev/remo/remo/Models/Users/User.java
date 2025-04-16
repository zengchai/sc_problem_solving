package dev.remo.remo.Models.Users;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
public class User implements UserDetails {
    
    private String id;
    private String name;
    private String email;
    private String password;
    private String nric;
    private String phoneNum;
    private String dob;
    private List<String> role;

    public User(String email, String password, List<String> roles) {
        this.email = email;
        this.password = password;
        this.role = roles;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.stream()
            .map(role -> new SimpleGrantedAuthority(role))
            .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return email;
    }

}
