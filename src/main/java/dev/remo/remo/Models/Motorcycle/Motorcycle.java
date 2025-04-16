package dev.remo.remo.Models.Motorcycle;

import java.util.List;

import dev.remo.remo.Models.Review.Review;
import lombok.Data;

@Data
public class Motorcycle {

    private String id;
    private String brand;
    private String model;
    private List<Review> reviews;
}
