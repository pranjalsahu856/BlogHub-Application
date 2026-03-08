package in.bloghub.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import in.bloghub.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
	boolean existsByCatName(String catName);
}
