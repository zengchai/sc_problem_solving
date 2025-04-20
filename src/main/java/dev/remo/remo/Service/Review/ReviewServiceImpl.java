package dev.remo.remo.Service.Review;

import org.springframework.beans.factory.annotation.Autowired;

import dev.remo.remo.Models.Review.Review;
import dev.remo.remo.Models.Review.ReviewDO;
import dev.remo.remo.Models.Users.User;
import dev.remo.remo.Repository.Review.ReviewRepository;
import dev.remo.remo.Service.User.UserService;
import dev.remo.remo.Utils.JwtUtils;

public class ReviewServiceImpl implements ReviewService{

    @Autowired
    ReviewRepository reviewRepository;

	@Autowired
	JwtUtils jwtUtils;

	@Autowired
	UserService userService;
    
    @Override
    public boolean addReview(Review review, String header) {
        // --- Precondition: Access token must be present ---
        assert header != null && header.startsWith("Bearer ") : "Precondition failed: Invalid Authorization header";
    
        String accessToken = header.substring(7);
        String email = jwtUtils.getEmailFromAccessToken(accessToken);
    
        // --- Invariant: Email must not be null or empty ---
        assert email != null && !email.isEmpty() : "Invariant failed: Email extracted from token must not be null";
    
        User user = (User) userService.loadUserByUsername(email);
    
        // --- Invariant: User must exist ---
        assert user != null : "Invariant failed: User must exist in system";
    
        // --- Precondition: Review content must not be null ---
        assert review.getReview() != null && !review.getReview().isEmpty() : "Precondition failed: Review content must not be empty";
    
        // Attach user to review
        review.setUser(user);
    
        // Save to database
        ReviewDO reviewDO = reviewRepository.insert(convertToDO(review));
    
        // --- Postcondition: Review must exist in repository after insert ---
        boolean reviewExists = reviewRepository.existsById(reviewDO.getId());
        assert reviewExists : "Postcondition failed: Review must exist after insertion";
    
        return true;
    }
    
    public ReviewDO convertToDO(Review review){
        return new ReviewDO(review.getBrand(),review.getModel(),review.getUser().getId(),review.getReview());
    }
}
