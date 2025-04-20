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
    public void addReview(Review review,String header) {

		String accessToken = header.substring(7);
		String email = jwtUtils.getEmailFromAccessToken(accessToken);

        System.err.println(email);
		User user = (User) userService.loadUserByUsername(email);

		review.setUser(user);
        reviewRepository.insert(convertToDO(review));
    }
    
    public ReviewDO convertToDO(Review review){
        return new ReviewDO(review.getBrand(),review.getModel(),review.getUser().getId(),review.getReview());
    }
}
