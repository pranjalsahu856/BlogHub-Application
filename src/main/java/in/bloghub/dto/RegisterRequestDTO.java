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
public class RegisterRequestDTO {
	@NotBlank(message="Name is Required")
	private String name;
	
	@NotBlank(message="Email is Required")
	@Email(message="Email format should be Valid")
	private String email;
	
	@NotBlank(message="Password is Required")
	@Size(min=6,message="Password must be atleast 6 Character long")
	private String password;
	
	@NotBlank(message="About is Required")
	private String about;
}
