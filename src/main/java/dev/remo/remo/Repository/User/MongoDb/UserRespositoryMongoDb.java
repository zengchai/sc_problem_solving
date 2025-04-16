package dev.remo.remo.Repository.User.MongoDb;

import org.springframework.beans.factory.annotation.Autowired;

import dev.remo.remo.Models.Users.UserDO;
import dev.remo.remo.Repository.User.UserRepository;

public class UserRespositoryMongoDb implements UserRepository {

    @Autowired
    UserMongoDb userMongoDb;

    public Boolean checkByName(String name) {
        return true;
    }


    public Boolean saveUser(UserDO user) {
        UserDO userDO = userMongoDb.save(user);
        if (userDO != null) {
            return true;
        } else {
            return false;
        }
    }

    public UserDO findByEmail(String email){
        return userMongoDb.findByEmail(email);
    }
}
