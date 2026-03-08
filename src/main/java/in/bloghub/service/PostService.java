package in.bloghub.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import in.bloghub.dto.PostRequestDTO;
import in.bloghub.dto.PostResponseDTO;
import in.bloghub.dto.PostUpdateDTO;
import in.bloghub.entity.Author;
import in.bloghub.entity.Category;
import in.bloghub.entity.Post;
import in.bloghub.exception.ResourceNotFoundException;
import in.bloghub.interceptor.SessionAuthInterceptor;
import in.bloghub.repository.AuthorRepository;
import in.bloghub.repository.CategoryRepository;
import in.bloghub.repository.PostRepository;

@Service
public class PostService {

	private final SessionAuthInterceptor sessionAuthInterceptor;
	private PostRepository postRepository;
	private AuthorRepository authorRepository;
	private CategoryRepository categoryRepository;

	@Autowired
	public PostService(PostRepository postRepository, AuthorRepository authorRepository,
			CategoryRepository categoryRepository, SessionAuthInterceptor sessionAuthInterceptor) {
		super();
		this.postRepository = postRepository;
		this.authorRepository = authorRepository;
		this.categoryRepository = categoryRepository;
		this.sessionAuthInterceptor = sessionAuthInterceptor;
	}

	public Post createPost(PostRequestDTO prDTO) {
		if (prDTO.getAuthorId() == null) {
			throw new RuntimeException("Author Id can not be null");
		}
		Author author = authorRepository.findById(prDTO.getAuthorId()).orElse(null);
		if (author == null) {
			throw new ResourceNotFoundException("Author not Found with id :" + prDTO.getAuthorId());
		}
		Category category = categoryRepository.findById(prDTO.getCategoryId()).orElse(null);
		if (category == null) {
			throw new ResourceNotFoundException("Category not Found with id :" + prDTO.getCategoryId());
		}
		Post post = new Post();
		post.setTitle(prDTO.getTitle());
		post.setContent(prDTO.getContent());
		post.setAuthor(author);
		post.setCategory(category);
		post.setCreatedAt(LocalDateTime.now());

		return postRepository.save(post);
	}

	public List<Post> getAllPost() {
		return postRepository.findAll();
	}

	public Page<PostResponseDTO> getAllPost(int size, int page, String sortBy, String sortDir) {
		Sort sort = sortDir.equalsIgnoreCase("DISC") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
		Pageable pageable = PageRequest.of(page, size, sort);
		Page<Post> postsPage = postRepository.findAll(pageable);
		List<PostResponseDTO> dtoList = new ArrayList<>();
		for (Post post : postsPage.getContent()) {
			PostResponseDTO responseDTO = new PostResponseDTO();
			responseDTO.setId(post.getId());
			responseDTO.setTitle(post.getTitle());
			responseDTO.setCategoryId(post.getCategory().getId());
			responseDTO.setCategoryName(post.getCategory().getCatName());
			responseDTO.setAuthorId(post.getAuthor().getId());
			responseDTO.setAuthorName(post.getAuthor().getName());
			responseDTO.setCreateDateTime(post.getCreatedAt());
			;
			responseDTO.setContent(post.getContent());
			dtoList.add(responseDTO);
		}
		Page<PostResponseDTO> pageList = new PageImpl<>(dtoList, pageable, postsPage.getTotalElements());
		return pageList;
	}

	public Post getPostById(Long postId) {
		Post post = postRepository.findById(postId).orElse(null);
		if (post == null) {
			throw new ResourceNotFoundException("Post not Found with id :" + postId);
		}
		return post;
	}

	public List<Post> searchPosts(String term) {
		return postRepository.findByTitleContainingOrContentContaining(term.toLowerCase(), term.toLowerCase());
	}

	public List<Post> getPostsByAuthor(Long authorId) {
		Author author = authorRepository.findById(authorId).orElse(null);
		if (author == null) {
			throw new ResourceNotFoundException("Author not Found with id :" + authorId);
		}
		return postRepository.findByAuthor(author);
	}

	public Post updatePost(Long postId, PostUpdateDTO postUpd) {
		Post post = getPostById(postId);
		if (postUpd == null || (postUpd.getTitle() == null && postUpd.getAuthorId() == null && postUpd.getCategoryId()==null
				&& postUpd.getContent() == null)) {
			throw new RuntimeException("At least one field must be Present for Updation");
		}

		if (postUpd.getTitle() != null) {
			post.setTitle(postUpd.getTitle());
		}
		if (postUpd.getContent() != null) {
			post.setContent(postUpd.getContent());
		}
		if (postUpd.getAuthorId() != null) {
			Author author = authorRepository.findById(postUpd.getAuthorId()).orElse(null);
			if (author == null) {
				throw new ResourceNotFoundException("Author not Found with id :" + postUpd.getAuthorId());
			}
			post.setAuthor(author);
		}
		if (postUpd.getCategoryId() != null) {
			Category category = categoryRepository.findById(postUpd.getCategoryId()).orElse(null);
			if (category == null) {
				throw new ResourceNotFoundException("Category not Found with id :" + postUpd.getCategoryId());
			}
			post.setCategory(category);
		}
		return postRepository.save(post);
	}
	
	public void deletePost(Long postId) {
		Post post = postRepository.findById(postId).orElse(null);
		if (post == null) {
			throw new ResourceNotFoundException("Post not Found with id :" + postId);
		}
		postRepository.delete(post);
	}

}
