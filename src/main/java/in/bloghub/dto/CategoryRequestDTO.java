package in.bloghub.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryRequestDTO {

	@NotBlank(message="Category Name is Required")
	private String catName;
	@NotBlank(message="Category Description is Required")
	private String desc;
}
