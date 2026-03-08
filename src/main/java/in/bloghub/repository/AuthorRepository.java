package in.bloghub.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import in.bloghub.entity.Author;

public interface AuthorRepository extends JpaRepository<Author, Long> {
	
	boolean existsByEmail(String email);

	Optional<Author> findByEmail(String email);

}
