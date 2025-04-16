package dev.remo.remo.Repository.User;

import dev.remo.remo.Models.Users.UserDO;

public interface UserRepository {
    
    Boolean checkByName(String name);
    Boolean saveUser(UserDO user);
    UserDO findByEmail(String email);

}
