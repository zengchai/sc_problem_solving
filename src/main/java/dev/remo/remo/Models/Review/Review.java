package dev.remo.remo.Models.Review;

import dev.remo.remo.Models.Users.User;
import lombok.Data;

@Data
public class Review {
    
    private String id;
    private String brand;
    private String model;
    private User user;
    private String review;
    
}
