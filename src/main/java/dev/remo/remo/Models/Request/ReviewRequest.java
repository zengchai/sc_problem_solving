package dev.remo.remo.Models.Request;

import dev.remo.remo.Models.Review.Review;
import dev.remo.remo.Models.Users.User;
import lombok.Data;

@Data
public class ReviewRequest {
    private String brand;
    private String model;
    private String review;

    public Review convertToReview() {
        return new Review(this.brand, this.model, this.review);
    }
}
