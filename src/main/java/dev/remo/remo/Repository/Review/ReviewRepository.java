package dev.remo.remo.Repository.Review;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import dev.remo.remo.Models.Review.ReviewDO;

public interface ReviewRepository extends MongoRepository<ReviewDO, ObjectId> {
    
}
