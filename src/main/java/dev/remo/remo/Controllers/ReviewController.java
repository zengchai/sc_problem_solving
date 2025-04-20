package dev.remo.remo.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import dev.remo.remo.Models.Response.GeneralResponse;
import dev.remo.remo.Models.Response.JwtResponse;
import dev.remo.remo.Models.Review.Review;
import dev.remo.remo.Models.Users.User;
import dev.remo.remo.Service.Review.ReviewService;
import dev.remo.remo.Service.User.UserService;
import dev.remo.remo.Utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/review")
public class ReviewController {

	@Autowired
	UserService userService;

	@Autowired
	ReviewService reviewService;

	@Autowired
	JwtUtils jwtUtils;

	@PostMapping("/create")
	public ResponseEntity<?> createReview(@Valid Review review, HttpServletRequest request) {
		String header = request.getHeader("Authorization");

		if (header == null || !header.startsWith("Bearer ")) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing or invalid Authorization header");
		}

		String accessToken = header.substring(7);
		String email = jwtUtils.getEmailFromAccessToken(accessToken);

		User user = (User) userService.loadUserByUsername(email);

		review.setUser(user);
		reviewService.addReview(review);

		return ResponseEntity.ok(
                GeneralResponse.builder()
                        .message("Review has been created successfully")
						.success(true)
						.build());
	}
}
