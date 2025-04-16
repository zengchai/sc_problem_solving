package dev.remo.remo.Models.Review;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Document(collection = "review")
@Data
public class ReviewDO {
        
    @Id
    private ObjectId id;

    @NotBlank
    private String motorcycleId;

    @NotBlank
    private String userId;

    @NotBlank
    private String review;
}
