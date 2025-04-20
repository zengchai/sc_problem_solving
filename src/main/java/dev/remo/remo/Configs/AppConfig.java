package dev.remo.remo.Configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import dev.remo.remo.Service.Review.ReviewService;
import dev.remo.remo.Service.Review.ReviewServiceImpl;
import dev.remo.remo.Service.User.UserService;
import dev.remo.remo.Service.User.UserServiceImpl;
import dev.remo.remo.Repository.User.UserRepository;
import dev.remo.remo.Repository.User.MongoDb.UserRespositoryMongoDb;

@Configuration
public class AppConfig {
    
    // Service Layer
    @Bean
    UserService userService(){
        return new UserServiceImpl();
    }

    @Bean
    ReviewService ReviewService(){
        return new ReviewServiceImpl();
    }

    // Repository Layer
    @Bean
    UserRepository userRepository(){
        return new UserRespositoryMongoDb();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
      return new BCryptPasswordEncoder(); // Returns a new instance of BCryptPasswordEncoder
    }
}
