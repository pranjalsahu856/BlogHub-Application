package in.bloghub.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostRequestDTO {
	@NotBlank(message="Title is Required")
	private String title;
	
	@NotBlank(message="Content is Required")
	private String content;
	
	@NotNull(message="Category is Required")
	private Long categoryId;
	
	private Long authorId;
}
