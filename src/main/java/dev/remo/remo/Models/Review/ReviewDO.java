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
    private String brand;

    @NotBlank
    private String model;

    @NotBlank
    private String userId;

    @NotBlank
    private String review;

    public ReviewDO(@NotBlank String brand, @NotBlank String model, @NotBlank String userId,
            @NotBlank String review) {
        this.brand = brand;
        this.model = model;
        this.userId = userId;
        this.review = review;
    }
}
