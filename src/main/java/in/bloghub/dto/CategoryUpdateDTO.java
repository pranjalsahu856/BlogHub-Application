package in.bloghub.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CategoryUpdateDTO {
	private String catName;
	private String desc;
}
