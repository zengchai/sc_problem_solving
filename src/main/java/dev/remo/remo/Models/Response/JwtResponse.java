package dev.remo.remo.Models.Response;

import java.util.List;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class JwtResponse extends GeneralResponse {
	private String token;
	private String email;
	private List<String> roles;
}
