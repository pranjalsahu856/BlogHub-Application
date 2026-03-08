package in.bloghub.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthorUpdateDTO {
	
	@Size(min =1,message="Name is Required")
	private String name;
	
	@Email(message="Email should be Valid")
	private String email;
	
	@Size(min=1,message="About is Required")
	private String about;
	
	
}
