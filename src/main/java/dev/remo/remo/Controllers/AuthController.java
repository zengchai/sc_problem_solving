package dev.remo.remo.Controllers;

import dev.remo.remo.Models.Request.SignInRequest;
import dev.remo.remo.Models.Request.SignUpRequest;
import dev.remo.remo.Models.Response.GeneralResponse;
import dev.remo.remo.Models.Response.JwtResponse;
import dev.remo.remo.Models.Users.User;
import dev.remo.remo.Service.User.UserService;
import dev.remo.remo.Utils.JwtUtils;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

// import javax.naming.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.core.AuthenticationException;


import dev.remo.remo.Models.Request.SignInRequest;
import dev.remo.remo.Models.Request.SignUpRequest;
import dev.remo.remo.Models.Response.JwtResponse;
import dev.remo.remo.Models.Users.User;
import dev.remo.remo.Service.User.UserService;
import dev.remo.remo.Utils.JwtUtils;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

        @Autowired
        UserService userService;

        @Autowired
        AuthenticationManager authenticationManager;

        @Autowired
        JwtUtils jwtUtils;

        // @PostMapping("/signup")
        // public ResponseEntity<?> register(@Valid @RequestBody SignUpRequest request)
        // {
        // try {
        // // --- Precondition: Email and password must not be null or empty ---
        // assert request.getEmail() != null && !request.getEmail().isEmpty() :
        // "Precondition failed: Email must not be null or empty";
        // assert request.getPassword() != null && !request.getPassword().isEmpty() :
        // "Precondition failed: Password must not be null or empty";

        // // --- Invariant: System must not allow duplicate emails ---
        // boolean emailExists = userService.checkByEmail(request.getEmail());
        // assert emailExists == userService.checkByEmail(request.getEmail()) :
        // "Invariant failed: Email existence check must be consistent";

        // if (emailExists) {
        // return ResponseEntity.ok(JwtResponse.builder()
        // .success(false)
        // .token("")
        // .error("Email already exists in the system")
        // .message("")
        // .build());
        // }

        // // --- Postcondition: After successful registration, the user should exist in
        // the system ---
        // boolean registered = userService.registerUser(request.convertToUser());

        // if (registered) {
        // assert userService.checkByEmail(request.getEmail()) : "Postcondition failed:
        // Registered user should exist in the system";

        // return ResponseEntity.ok(JwtResponse.builder()
        // .success(true)
        // .token("")
        // .error("")
        // .message("Register Successful")
        // .build());
        // }

        // // --- Postcondition: If registration fails, user should still not exist ---
        // assert !userService.checkByEmail(request.getEmail()) : "Postcondition failed:
        // Failed registration should not create user";

        // } catch (AssertionError ae) {
        // // Handle failed assertion and return meaningful error to frontend
        // return ResponseEntity.ok().body(JwtResponse.builder()
        // .success(false)
        // .token("")
        // .error("Assertion Error: " + ae.getMessage())
        // .message("")
        // .build());
        // } catch (Exception e) {
        // // General error fallback
        // return ResponseEntity.internalServerError().body(JwtResponse.builder()
        // .success(false)
        // .token("")
        // .error("Unexpected error: " + e.getMessage())
        // .message("")
        // .build());
        // }

        // // Final fallback (in case assertions and registration both fail silently)
        // return ResponseEntity.badRequest().body(JwtResponse.builder()
        // .success(false)
        // .token("")
        // .error("Register Unsuccessful")
        // .message("")
        // .build());
        // }

        // Question 3: Coding Example of Design by Contract for signup
        @PostMapping("/signup")
        public ResponseEntity<?> register(@Valid @RequestBody SignUpRequest request) {
                // --- Precondition: Email and password must not be null ---
                if (request.getEmail() == null || request.getEmail().isEmpty()) {
                        // If the email is null or empty, we throw a ResponseStatusException with a
                        // BAD_REQUEST status (400)
                        // to inform the client that this input is invalid.
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                        "Precondition failed: Email must not be null or empty.");
                }
                if (request.getPassword() == null || request.getPassword().isEmpty()) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                        "Precondition failed: Password must not be null or empty.");
                }

                // --- Invariant: Email must be unique in the system ---
                boolean emailExists = userService.checkByEmail(request.getEmail());
                if (emailExists) {
                        // If the email exists, we throw a ResponseStatusException with a CONFLICT
                        // status (409),
                        // indicating that the email is already registered in the system.
                        throw new ResponseStatusException(HttpStatus.CONFLICT,
                                        "Invariant failed: Email already exists in the system.");
                }

                // --- Process: Register the user ---
                boolean registered = userService.registerUser(request.convertToUser());

                // --- Postcondition: After successful registration, the user should exist in
                // the system ---
                if (registered) {
                        if (!userService.checkByEmail(request.getEmail())) {
                                // If the email does not exist in the system, it means the registration failed.
                                // We throw an INTERNAL_SERVER_ERROR (500) as the postcondition has not been
                                // met.
                                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                                                "Postcondition failed: Registered user should exist in system.");
                        }

                        return ResponseEntity.ok(JwtResponse.builder()
                                        .success(true)
                                        .token("")
                                        .error("")
                                        .message("Register Successful")
                                        .build());
                }

                // --- Postcondition: If registration fails, the user should not exist ---
                if (userService.checkByEmail(request.getEmail())) {
                        // If the email still exists in the system after a failed registration, it means
                        // the postcondition has failed,
                        // so we throw an INTERNAL_SERVER_ERROR (500).
                        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                                        "Postcondition failed: Failed registration should not create user.");
                }

                return ResponseEntity.ok(JwtResponse.builder()
                                .success(false)
                                .token("")
                                .error("Registration Unsuccessful")
                                .message("")
                                .build());
        }

        /*@PostMapping("/signin")
        public ResponseEntity<?> login(@RequestBody SignInRequest request, HttpServletResponse response) {
                try {
                        // --- Precondition: Email and password must not be null or empty ---
                        assert request.getEmail() != null && !request.getEmail().isEmpty()
                                        : "Precondition failed: Email must not be null or empty";
                        assert request.getPassword() != null && !request.getPassword().isEmpty()
                                        : "Precondition failed: Password must not be null or empty";

                        // --- Authenticate the user ---
                        Authentication authentication = authenticationManager.authenticate(
                                        new UsernamePasswordAuthenticationToken(
                                                        request.getEmail(),
                                                        request.getPassword()));

                        SecurityContextHolder.getContext().setAuthentication(authentication);

                        // --- Invariant: Authenticated user's email must match login request email ---
                        User user = (User) authentication.getPrincipal();
                        // Simulate an authenticated user with a different email for Invariant error
                        // List<String> roles = List.of("USER"); // Simulate roles
                        // User user = new User("wrongEmail@example.com", "dummyPassword", roles);
                        // user.setEmail("wrongEmail@example.com");
                        assert user.getEmail().equals(request.getEmail())
                                        : "Invariant failed: Authenticated user's email must match login email";

                        // --- Generate tokens ---
                        String accessToken = jwtUtils.generateAccessToken(authentication);
                        // Generate Assertion Error
                        // String accessToken = "";

                        String refreshToken = jwtUtils.generateRefreshToken(authentication);
                        // Generate Assertion Error
                        // String refreshToken = "";

                        // --- Postcondition: Tokens must be generated successfully ---
                        assert accessToken != null && !accessToken.isEmpty()
                                        : "Postcondition failed: Access token must be generated";
                        assert refreshToken != null && !refreshToken.isEmpty()
                                        : "Postcondition failed: Refresh token must be generated";

                        // Set refresh token cookie
                        jwtUtils.setJwtCookie(response, refreshToken);

                        return ResponseEntity.ok(JwtResponse.builder()
                                        .success(true)
                                        .token(accessToken)
                                        .email(user.getEmail())
                                        .roles(user.getRole())
                                        .error("")
                                        .message("Login successful")
                                        .build());

                } catch (AssertionError ae) {
                        // Custom assertion/invariant violation handler
                        return ResponseEntity.ok().body(JwtResponse.builder()
                                        .success(false)
                                        .token("")
                                        .error("Assertion Error: " + ae.getMessage())
                                        .message("")
                                        .build());
                } catch (BadCredentialsException be) {
                        // Spring Security authentication failure
                        return ResponseEntity.ok().body(JwtResponse.builder()
                                        .success(false)
                                        .token("")
                                        .error("Invalid email or password")
                                        .message("")
                                        .build());
                } catch (Exception e) {
                        // General error fallback
                        return ResponseEntity.internalServerError().body(JwtResponse.builder()
                                        .success(false)
                                        .token("")
                                        .error("Unexpected error: " + e.getMessage())
                                        .message("")
                                        .build());
                }
        }

        @PostMapping("/signin")
        public ResponseEntity<?> login(@Valid @RequestBody SignInRequest request, HttpServletResponse response) {
          // --- Precondition: Email and password must not be null or empty ---
          if (request.getEmail() == null || request.getEmail().isEmpty()) {
               throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Precondition failed: Email must not be null or empty.");
             }
          if (request.getPassword() == null || request.getPassword().isEmpty()) {
               throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Precondition failed: Password must not be null or empty.");
             }

          // --- Invariant: AuthenticationManager must be available ---
          if (authenticationManager == null) {
              throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Invariant failed: AuthenticationManager is not available.");
             }

           // --- Process: Authenticate user ---
           Authentication authentication;
           try {
                authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
               } catch (AuthenticationException ex) {
                 throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Login failed: Invalid credentials.");
            }

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // --- Process: Generate access and refresh tokens ---
            String accessToken = jwtUtils.generateAccessToken(authentication);
            String refreshToken = jwtUtils.generateRefreshToken(authentication);

            // --- Postcondition: Token must not be null or empty ---
            if (accessToken == null || accessToken.isEmpty()) {
               throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Postcondition failed: Access token must not be null or empty.");
               }

            if (refreshToken == null || refreshToken.isEmpty()) {
               throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Postcondition failed: Refresh token must not be null or empty.");
               }

            jwtUtils.setJwtCookie(response, refreshToken);

            // --- Process: Extract user info from authentication ---
            User user = (User) authentication.getPrincipal();

            // --- Return success response ---
            return ResponseEntity.ok(
                JwtResponse.builder()
                    .token(accessToken)
                    .email(user.getEmail())
                    .roles(user.getRole())
                    .success(true)
                    .error("")
                    .message("Login successful")
                    .build());
}

        @PostMapping("/signin")
        public ResponseEntity<?> login(@Valid @RequestBody SignInRequest request, HttpServletResponse response) {
          // --- Precondition: Email and password must not be null or empty ---
          if (request.getEmail() == null || request.getEmail().isEmpty()) {
              throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Precondition failed: Email must not be null or empty.");
          }
          if (request.getPassword() == null || request.getPassword().isEmpty()) {
              throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Precondition failed: Password must not be null or empty.");
          }

          // --- Invariant: AuthenticationManager must be available ---
          if (authenticationManager == null) {
             throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Invariant failed: AuthenticationManager is not available.");
           }

          // --- Process: Authenticate user ---
            Authentication authentication;
            try {
                authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
                } catch (AuthenticationException ex) {
                 throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Login failed: Invalid credentials.");
                }

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // --- Process: Generate access and refresh tokens ---
            String accessToken = jwtUtils.generateAccessToken(authentication);
            String refreshToken = jwtUtils.generateRefreshToken(authentication);

            // --- Postcondition: Token must not be null or empty ---
            if (accessToken == null || accessToken.isEmpty()) {
               throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Postcondition failed: Access token must not be null or empty.");
            }

            if (refreshToken == null || refreshToken.isEmpty()) {
               throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Postcondition failed: Refresh token must not be null or empty.");
            }

            jwtUtils.setJwtCookie(response, refreshToken);

            // --- Process: Extract user info from authentication ---
            User user = (User) authentication.getPrincipal();

            // --- Return success response ---
            return ResponseEntity.ok(
               JwtResponse.builder()
                    .token(accessToken)
                    .email(user.getEmail())
                    .roles(user.getRole())
                    .success(true)
                    .error("")
                    .message("Login successful")
                    .build());
        }



        // Refresh access token using refresh token
        @PostMapping("/refresh")
        public ResponseEntity<?> refreshToken(
                        @CookieValue(name = "refreshToken", required = false) String refreshToken,
                        HttpServletResponse response) {
                if (refreshToken == null || !jwtUtils.validateRefreshToken(refreshToken)) {
                        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh token");
                }

                User user = (User) userService.loadUserByUsername(jwtUtils.getEmailFromRefreshToken(refreshToken));

                // Generate new tokens
                Authentication authentication = new UsernamePasswordAuthenticationToken(user, "",
                                user.getAuthorities());
                String newAccessToken = jwtUtils.generateAccessToken(authentication);
                String newRefreshToken = jwtUtils.generateRefreshToken(authentication);

                // Update refresh token cookie
                jwtUtils.setJwtCookie(response, newRefreshToken);

                return ResponseEntity.ok(
                                JwtResponse.builder()
                                                .token(newAccessToken)
                                                .email(user.getEmail())
                                                .roles(user.getRole())
                                                .success(true)
                                                .error("")
                                                .message("")
                                                .build());
        }

        @PostMapping("/signout")
        public ResponseEntity<?> logoutUser(HttpServletResponse response) {
                jwtUtils.cleanJwtCookie(response);
                return ResponseEntity.ok("Logged out successfully");
        }

        // --- Postcondition: If registration fails, the user should not exist ---
        if (userService.checkByEmail(request.getEmail())) {
            // If the email still exists in the system after a failed registration, it means the postcondition has failed,
            // so we throw an INTERNAL_SERVER_ERROR (500).
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Postcondition failed: Failed registration should not create user.");
        }

        return ResponseEntity.ok(JwtResponse.builder()
                .success(false)
                .token("")
                .error("Registration Unsuccessful")
                .message("")
                .build());
    }*/

    @PostMapping("/signin")
    public ResponseEntity<?> login(@Valid @RequestBody SignInRequest request, HttpServletResponse response) {
        // --- Precondition: Email and password must not be null or empty ---
        if (request.getEmail() == null || request.getEmail().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Precondition failed: Email must not be null or empty.");
        }
        if (request.getPassword() == null || request.getPassword().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Precondition failed: Password must not be null or empty.");
        }
    
        // --- Invariant: AuthenticationManager must be available ---
        if (authenticationManager == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Invariant failed: AuthenticationManager is not available.");
        }
    
        // --- Process: Authenticate user ---
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        } catch (AuthenticationException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Login failed: Invalid credentials.");
        }
    
        SecurityContextHolder.getContext().setAuthentication(authentication);
    
        // --- Process: Generate access and refresh tokens ---
        String accessToken = jwtUtils.generateAccessToken(authentication);
        String refreshToken = jwtUtils.generateRefreshToken(authentication);
    
        // --- Postcondition: Token must not be null or empty ---
        if (accessToken == null || accessToken.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Postcondition failed: Access token must not be null or empty.");
        }
    
        if (refreshToken == null || refreshToken.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Postcondition failed: Refresh token must not be null or empty.");
        }
    
        jwtUtils.setJwtCookie(response, refreshToken);
    
        // --- Process: Extract user info from authentication ---
        User user = (User) authentication.getPrincipal();
    
        // --- Return success response ---
        return ResponseEntity.ok(
                JwtResponse.builder()
                        .token(accessToken)
                        .email(user.getEmail())
                        .roles(user.getRole())
                        .success(true)
                        .error("")
                        .message("Login successful")
                        .build());
    }
    
        
    /*@PostMapping("/signin")
    public ResponseEntity<?> login(
            @Valid @RequestBody SignInRequest request, HttpServletResponse response) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = jwtUtils.generateAccessToken(authentication);
        String refreshToken = jwtUtils.generateRefreshToken(authentication);
        jwtUtils.setJwtCookie(response, refreshToken);

        // Get user details from the authentication object
        User user = (User) authentication.getPrincipal();

        // Return a response containing the JWT and user details
        return ResponseEntity.ok(
                JwtResponse.builder()
                        .token(accessToken)
                        .email(user.getEmail())
                        .roles(user.getRole())
                        .success(true)
                        .error("")
                        .message("")
                        .build());
    } */

    // Refresh access token using refresh token
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(
            @CookieValue(name = "refreshToken", required = false) String refreshToken,
            HttpServletResponse response) {
        if (refreshToken == null || !jwtUtils.validateRefreshToken(refreshToken)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh token");
        }

        User user = (User) userService.loadUserByUsername(jwtUtils.getEmailFromRefreshToken(refreshToken));
        
        // Generate new tokens
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, "", user.getAuthorities());
        String newAccessToken = jwtUtils.generateAccessToken(authentication);
        String newRefreshToken = jwtUtils.generateRefreshToken(authentication);

        // Update refresh token cookie
        jwtUtils.setJwtCookie(response, newRefreshToken);

        return ResponseEntity.ok(
                JwtResponse.builder()
                        .token(newAccessToken)
                        .email(user.getEmail())
                        .roles(user.getRole())
                        .success(true)
                        .error("")
                        .message("")
                        .build());
    }

    @PostMapping("/signout")
    public ResponseEntity<?> logoutUser(HttpServletResponse response) {
        jwtUtils.cleanJwtCookie(response);
        return ResponseEntity.ok("Logged out successfully");
    }
}

