package in.bloghub.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import in.bloghub.dto.PostRequestDTO;
import in.bloghub.dto.PostResponseDTO;
import in.bloghub.dto.PostUpdateDTO;
import in.bloghub.entity.Post;
import in.bloghub.repository.CategoryRepository;
import in.bloghub.service.PostService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/posts")
public class PostController {

	private CategoryRepository categoryRepository;
	private PostService postService;

	@Autowired
	public PostController(PostService postService, CategoryRepository categoryRepository) {
		this.postService = postService;
		this.categoryRepository = categoryRepository;
	}

	@PostMapping("/create")
	public ResponseEntity<PostResponseDTO> createPost(@RequestBody @Valid PostRequestDTO requestDTO,
			HttpSession session) {
		Long currentUserId = (Long) session.getAttribute("userId");
		requestDTO.setAuthorId(currentUserId);
		Post post = postService.createPost(requestDTO);
		PostResponseDTO responseDTO = new PostResponseDTO();
		responseDTO.setId(post.getId());
		responseDTO.setTitle(post.getTitle());
		responseDTO.setContent(post.getContent());
		responseDTO.setCategoryId(post.getCategory().getId());
		responseDTO.setAuthorId(post.getAuthor().getId());
		responseDTO.setCreateDateTime(post.getCreatedAt());
		responseDTO.setCategoryName(post.getCategory().getCatName());
		responseDTO.setAuthorName(post.getAuthor().getName());

		return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
	}

	@GetMapping("/getAll")
	public ResponseEntity<List<PostResponseDTO>> getAllPosts(@RequestParam(required = false) String term) {
		List<Post> postList;
		if (term != null && !term.isBlank()) {
			postList = postService.searchPosts(term);
		} else {
			postList = postService.getAllPost();
		}
		List<PostResponseDTO> responseList = new ArrayList<>();
		for (Post post : postList) {
			PostResponseDTO responseDTO = new PostResponseDTO();
			responseDTO.setId(post.getId());
			responseDTO.setTitle(post.getTitle());
			responseDTO.setContent(post.getContent());
			responseDTO.setCategoryId(post.getCategory().getId());
			responseDTO.setAuthorId(post.getAuthor().getId());
			responseDTO.setCreateDateTime(post.getCreatedAt());
			responseDTO.setCategoryName(post.getCategory().getCatName());
			responseDTO.setAuthorName(post.getAuthor().getName());
			responseList.add(responseDTO);
		}
		return ResponseEntity.ok(responseList);
	}

	@GetMapping
	public ResponseEntity<Page<PostResponseDTO>> getAll(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "3") int size, @RequestParam(defaultValue = "createdAt") String sortBy,
			@RequestParam(defaultValue = "DESC") String sortDir) {
		return ResponseEntity.ok(postService.getAllPost(size,page, sortBy, sortDir));
	}

	@GetMapping("/{id}")
	public ResponseEntity<PostResponseDTO> getPostById(@PathVariable("id") Long id) {
		Post post = postService.getPostById(id);
		PostResponseDTO responseDTO = new PostResponseDTO();
		responseDTO.setId(post.getId());
		responseDTO.setTitle(post.getTitle());
		responseDTO.setContent(post.getContent());
		responseDTO.setCategoryId(post.getCategory().getId());
		responseDTO.setAuthorId(post.getAuthor().getId());
		responseDTO.setCreateDateTime(post.getCreatedAt());
		responseDTO.setCategoryName(post.getCategory().getCatName());
		responseDTO.setAuthorName(post.getAuthor().getName());
		return ResponseEntity.ok(responseDTO);
	}
	
	@GetMapping("/my-posts")
	public ResponseEntity<List<PostResponseDTO>> getMyPosts(@RequestAttribute("currentUserId") Long currentUserId) {
		List<Post> postList = postService.getPostsByAuthor(currentUserId);
		List<PostResponseDTO> responseList = new ArrayList<>();
		for (Post post : postList) {
			PostResponseDTO responseDTO = new PostResponseDTO();
			responseDTO.setId(post.getId());
			responseDTO.setTitle(post.getTitle());
			responseDTO.setContent(post.getContent());
			responseDTO.setCategoryId(post.getCategory().getId());
			responseDTO.setAuthorId(post.getAuthor().getId());
			responseDTO.setCreateDateTime(post.getCreatedAt());
			responseDTO.setCategoryName(post.getCategory().getCatName());
			responseDTO.setAuthorName(post.getAuthor().getName());
			responseList.add(responseDTO);
		}
		return ResponseEntity.ok(responseList);
	}
	
	@PutMapping("/update/{id}")
	public ResponseEntity<?> updatePost(
			@PathVariable Long id,
			@RequestBody PostUpdateDTO postUpdDTO,
			@RequestAttribute("currentUserId")Long currentUserId,
			@RequestAttribute("currentUserRole")String currentUserRole
			){
		Post post = postService.getPostById(id);
		if(!post.getAuthor().getId().equals(currentUserId) && !currentUserRole.equals("ADMIN")) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("{\"error\":\"You can update only your own Posts\"}");
		}
		Post updatedPost = postService.updatePost(id, postUpdDTO);
		PostResponseDTO responseDTO = new PostResponseDTO();
		responseDTO.setId(updatedPost.getId());
		responseDTO.setTitle(updatedPost.getTitle());
		responseDTO.setContent(updatedPost.getContent());
		responseDTO.setCategoryId(updatedPost.getCategory().getId());
		responseDTO.setAuthorId(updatedPost.getAuthor().getId());
		responseDTO.setCreateDateTime(updatedPost.getCreatedAt());
		responseDTO.setCategoryName(updatedPost.getCategory().getCatName());
		responseDTO.setAuthorName(updatedPost.getAuthor().getName());
		return ResponseEntity.ok(responseDTO);
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> deletePost(
			@PathVariable Long id,
			@RequestAttribute("currentUserId")Long currentUserId,
			@RequestAttribute("currentUserRole")String currentUserRole) {
		Post post = postService.getPostById(id);
		if(!post.getAuthor().getId().equals(currentUserId) && !currentUserRole.equals("ADMIN")) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("{\"error\":\"You can Delete only your own Posts\"}");
		}
		postService.deletePost(id);
		return ResponseEntity.ok("Post Deleted Successfully");
		
	}
	

}
