package dev.remo.remo.Service.Review;

import org.springframework.beans.factory.annotation.Autowired;

import dev.remo.remo.Models.Review.Review;
import dev.remo.remo.Models.Review.ReviewDO;
import dev.remo.remo.Repository.Review.ReviewRepository;

public class ReviewServiceImpl implements ReviewService{

    @Autowired
    ReviewRepository reviewRepository;

    @Override
    public void addReview(Review review) {

        reviewRepository.insert(convertToDO(review));
        
    }
    
    public ReviewDO convertToDO(Review review){
        return new ReviewDO(review.getBrand(),review.getModel(),review.getUser().getId(),review.getReview());
    }
}
