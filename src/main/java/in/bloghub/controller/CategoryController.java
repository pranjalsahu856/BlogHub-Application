package in.bloghub.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.bloghub.dto.CategoryRequestDTO;
import in.bloghub.dto.CategoryResponseDTO;
import in.bloghub.dto.CategoryUpdateDTO;
import in.bloghub.service.CategoryService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
	private CategoryService categoryService;

	@Autowired
	public CategoryController(CategoryService categoryService) {
		this.categoryService = categoryService;
	}

	@PostMapping("/create")
	public ResponseEntity<CategoryResponseDTO> createDTO(@Valid @RequestBody CategoryRequestDTO reqDTO) {
		CategoryResponseDTO responseDTO = categoryService.createCategory(reqDTO);
		return new ResponseEntity<CategoryResponseDTO>(responseDTO,HttpStatus.CREATED);
	}
	
	@GetMapping
	public ResponseEntity<List<CategoryResponseDTO>> getAllCategories(){
		List<CategoryResponseDTO> responseList = categoryService.getAllCategories();
		return ResponseEntity.ok(responseList);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<CategoryResponseDTO> getCategoryById(@PathVariable Long id) {
		CategoryResponseDTO responseDTO = categoryService.getCategoryById(id);
		return ResponseEntity.ok(responseDTO);
	}
	
	@PutMapping("/update/{id}")
	public ResponseEntity<CategoryResponseDTO> updateCategory(@PathVariable Long id, @RequestBody CategoryUpdateDTO updDtO){
		return ResponseEntity.ok(categoryService.updateCategory(id, updDtO));
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
		categoryService.deleteCategory(id);
		return ResponseEntity.ok("Category Deleted Successfully");
	}

}
