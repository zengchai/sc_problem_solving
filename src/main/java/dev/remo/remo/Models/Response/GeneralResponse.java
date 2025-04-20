package dev.remo.remo.Models.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class GeneralResponse {
    Boolean success;
    String error;
    String message;
}
