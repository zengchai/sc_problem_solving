package dev.remo.remo.Service.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import dev.remo.remo.Models.Users.User;
import dev.remo.remo.Models.Users.UserDO;
import dev.remo.remo.Repository.User.UserRepository;

public class UserServiceImpl implements UserService {
    
    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    public Boolean checkByName(String name){
        return userRepository.checkByName(name);
    }

    public Boolean checkByEmail(String email){
        UserDO userDO = userRepository.findByEmail(email);
        if (userDO != null) {
            return true;
        } else {
            return false;
        }
    }

    public Boolean registerUser(User user){
        // Encode the password
        user.setPassword(encoder.encode(user.getPassword()));

        // Save the user to db
        return userRepository.saveUser(convertToUserDO(user));

    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Fetch user by email (instead of username)
        User user = convertToUser(userRepository.findByEmail(email));
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        // Return the User object (which implements UserDetails)
        return user;
    }

    public UserDO convertToUserDO(User user){
        return new UserDO(user.getEmail(),user.getPassword(),user.getRole());
    }

    public User convertToUser(UserDO userDO){
        return new User(userDO.getEmail(),userDO.getPassword(),userDO.getRole());
    }
}
