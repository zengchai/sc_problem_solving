package dev.remo.remo.Models.Users;

import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Document(collection = "user")
@Data
public class UserDO {
    
    @Id
    private ObjectId id;

    @NotBlank
    private String name;
    
    @NotBlank
    private String email;
    
    @NotBlank
    private String password;

    @NotBlank
    private List<String> role;

    private String nric;
    private String phoneNum;
    private Date dob;
     
    public UserDO(String email, String password, List<String> role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }
}
