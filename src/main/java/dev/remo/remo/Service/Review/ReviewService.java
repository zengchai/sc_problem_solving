package dev.remo.remo.Service.Review;

import dev.remo.remo.Models.Review.Review;

public interface ReviewService {
    boolean addReview(Review review,String header);
}
