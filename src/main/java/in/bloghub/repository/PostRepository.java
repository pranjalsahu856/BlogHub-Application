package in.bloghub.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import in.bloghub.entity.Author;
import in.bloghub.entity.Post;


public interface PostRepository extends JpaRepository<Post, Long>  {
	List<Post> findByAuthor(Author author);
	List<Post> findByTitleContainingOrContentContaining(String title,String content);

}
