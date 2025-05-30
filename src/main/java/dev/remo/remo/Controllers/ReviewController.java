package dev.remo.remo.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import dev.remo.remo.Models.Request.ReviewRequest;
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
	ReviewService reviewService;

	@PostMapping("/create")
	public ResponseEntity<?> createReview(@Valid @RequestBody ReviewRequest reviewRequest, HttpServletRequest request) {
		try {
			// --- Precondition: Authorization header must be present and valid ---
			String header = request.getHeader("Authorization");
			assert header != null && header.startsWith("Bearer ") : "Precondition failed: Missing or invalid Authorization header";
	
			// --- Precondition: Review content must not be null or empty ---
			assert reviewRequest.getReview() != null && !reviewRequest.getReview().isEmpty()
				: "Precondition failed: Review content must not be empty";
	
			// --- Call service ---
			boolean saved = reviewService.addReview(reviewRequest.convertToReview(), header);
	
			// --- Postcondition: Review must be saved successfully ---
			assert saved : "Postcondition failed: Review was not saved";
	
			return ResponseEntity.ok(
				GeneralResponse.builder()
					.message("Review has been created successfully")
					.success(true)
					.build()
			);
		} catch (AssertionError ae) {
			// Handles contract violations (pre, post, invariant)
			return ResponseEntity.ok().body(
				GeneralResponse.builder()
					.message("Assertion failed: " + ae.getMessage())
					.success(false)
					.build()
			);
		} catch (Exception e) {
			// Handles unexpected server errors
			return ResponseEntity.internalServerError().body(
				GeneralResponse.builder()
					.message("Unexpected error: " + e.getMessage())
					.success(false)
					.build()
			);
		}
	}

	// Question 3 Feature 3: Implementation Design By Contract for Add Review Feature

//	@PostMapping("/create")
//	public ResponseEntity<?> createReview(@Valid @RequestBody ReviewRequest reviewRequest, HttpServletRequest request) {
//		try {
//			// === PRECONDITIONS ===
//			// Preconditions define what must be true before executing the core logic.
//			// These checks ensure that required input and request headers are valid.
//			String header = request.getHeader("Authorization");
//			if (header == null || !header.startsWith("Bearer ")) {
//				// If the authorization header is missing or malformed, the contract is violated.
//				throw new ResponseStatusException(
//						HttpStatus.BAD_REQUEST, "Precondition failed: Missing or invalid Authorization header");
//			}
//
//			if (reviewRequest.getReview() == null || reviewRequest.getReview().trim().isEmpty()) {
//				// Input review text must not be null or empty
//				throw new ResponseStatusException(
//						HttpStatus.BAD_REQUEST, "Precondition failed: Review content must not be empty");
//			}
//
//			// === INVARIANTS ===
//			// Invariants are conditions that must always hold true during method execution.
//			// Here, we enforce constraints on the review's length, ensuring consistent business rules.
//			String reviewText = reviewRequest.getReview().trim();
//			if (reviewText.length() < 10 || reviewText.length() > 1000) {
//				throw new ResponseStatusException(
//						HttpStatus.BAD_REQUEST, "Invariant failed: Review must be between 10 and 1000 characters");
//			}
//
//			// === PROCESS ===
//			// Core functionality: this is where the actual business logic occurs.
//			// The controller delegates to the service layer to persist the review.
//			boolean saved = reviewService.addReview(reviewRequest.convertToReview(), header);
//
//			// === POSTCONDITIONS ===
//			// Postconditions verify that after the method completes, certain conditions must be true.
//			// In this case, we check that the review was successfully saved.
//			if (!saved) {
//				throw new ResponseStatusException(
//						HttpStatus.INTERNAL_SERVER_ERROR, "Postcondition failed: Review was not saved");
//			}
//
//			// If all checks pass, return a success response
//			return ResponseEntity.ok(
//					GeneralResponse.builder()
//							.message("Review has been created successfully")
//							.success(true)
//							.build()
//			);
//
//		} catch (ResponseStatusException ex) {
//			throw ex;
//		} catch (Exception e) {
//			throw new ResponseStatusException(
//					HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error: " + e.getMessage());
//		}
//	}
}
