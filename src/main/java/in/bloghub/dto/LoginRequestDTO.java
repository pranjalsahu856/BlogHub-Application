package in.bloghub.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDTO {
	@NotBlank(message="Email is Required")
	@Email(message="Email should be Valid")
	private String email;
	
	@NotBlank(message="Password is Required")
	private String password;
}
