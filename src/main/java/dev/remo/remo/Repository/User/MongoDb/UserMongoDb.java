package dev.remo.remo.Repository.User.MongoDb;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import dev.remo.remo.Models.Users.UserDO;

public interface UserMongoDb extends MongoRepository<UserDO,ObjectId> {
    UserDO findByEmail(String email); 
}
