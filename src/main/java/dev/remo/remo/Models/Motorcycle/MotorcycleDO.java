package dev.remo.remo.Models.Motorcycle;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document(collection = "motorcycle")
@Data
public class MotorcycleDO {
    
    @Id
    private ObjectId id;
    private String brand;
    private String model;
}
