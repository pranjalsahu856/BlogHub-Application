package in.bloghub.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.bloghub.dto.CategoryRequestDTO;
import in.bloghub.dto.CategoryResponseDTO;
import in.bloghub.dto.CategoryUpdateDTO;
import in.bloghub.entity.Category;
import in.bloghub.exception.ResourceAlreadyExistsException;
import in.bloghub.exception.ResourceNotFoundException;
import in.bloghub.repository.CategoryRepository;

@Service
public class CategoryService {
	private CategoryRepository categoryRepository;

	@Autowired
	public CategoryService(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}

	public CategoryResponseDTO createCategory(CategoryRequestDTO catRequest) {
		if (categoryRepository.existsByCatName(catRequest.getCatName())) {
			throw new ResourceAlreadyExistsException("This Category name is Already Present");
		}
		Category category = new Category();
		category.setCatName(catRequest.getCatName());
		category.setDescription(catRequest.getDesc());
		Category catSaved = categoryRepository.save(category);
		CategoryResponseDTO responseDTO = new CategoryResponseDTO();
		responseDTO.setId(catSaved.getId());
		responseDTO.setCatName(catSaved.getCatName());
		responseDTO.setDesc(catSaved.getDescription());
		return responseDTO;
	}

	public List<CategoryResponseDTO> getAllCategories() {
		List<Category> categoryList = categoryRepository.findAll();
		List<CategoryResponseDTO> responseDTOList = new ArrayList<>();
		for (Category category : categoryList) {
			CategoryResponseDTO responseDTO = new CategoryResponseDTO();
			responseDTO.setId(category.getId());
			responseDTO.setCatName(category.getCatName());
			responseDTO.setDesc(category.getDescription());
			responseDTOList.add(responseDTO);
		}
		return responseDTOList;
	}
	
	public CategoryResponseDTO getCategoryById(Long id) {
		Category cat = categoryRepository.findById(id).orElse(null);
		if(cat==null) {
			throw new ResourceNotFoundException("Category not found with Id :"+id);
		}
		return new CategoryResponseDTO(id,cat.getCatName(),cat.getDescription());
	}
	
	public CategoryResponseDTO updateCategory(Long id,CategoryUpdateDTO updDTO) {
		Category cat = categoryRepository.findById(id).orElse(null);
		if(cat==null) {
			throw new ResourceNotFoundException("Category not found with Id :"+id);
		}
		if(updDTO==null || (updDTO.getCatName()==null && updDTO.getDesc()==null)) {
			throw new IllegalArgumentException("At least one field must be present for Updation");
		}
		if(updDTO.getCatName()!=null) {
			cat.setCatName(updDTO.getCatName());
		}
		if(updDTO.getDesc()!=null) {
			cat.setDescription(updDTO.getDesc());
		}
		Category updatedCat = categoryRepository.save(cat);
		CategoryResponseDTO responseDTO = new CategoryResponseDTO();
		responseDTO.setId(updatedCat.getId());
		responseDTO.setCatName(updatedCat.getCatName());
		responseDTO.setDesc(updatedCat.getDescription());
		return responseDTO;
	}
	
	public void deleteCategory(Long id) {
		Category cat = categoryRepository.findById(id).orElse(null);
		if(cat==null) {
			throw new ResourceNotFoundException("Category not found with Id :"+id);
		}
		categoryRepository.delete(cat);
	}
}
