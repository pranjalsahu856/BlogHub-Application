package in.bloghub.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.bloghub.dto.AuthorResponseDTO;
import in.bloghub.dto.AuthorUpdateDTO;
import in.bloghub.entity.Author;
import in.bloghub.service.AuthorService;

@RestController
@RequestMapping("/api/users")
public class AuthorController {
	private AuthorService authorService;

	@Autowired
	public AuthorController(AuthorService authorService) {
		this.authorService = authorService;
	}

	@GetMapping("/{id}")
	public ResponseEntity<AuthorResponseDTO> getUserById(@PathVariable Long id) {
		Author author = authorService.getUserById(id);
		AuthorResponseDTO authorResponse = new AuthorResponseDTO(id, author.getName(), author.getEmail(),
				author.getRole(), author.getAbout());
		return ResponseEntity.ok(authorResponse);
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody AuthorUpdateDTO authUpdate,
			@RequestAttribute("currentUserId") Long currentUserId,
			@RequestAttribute("currentUserRole") String currentUserRole) {

		if (!id.equals(currentUserId) && currentUserRole.equals("ADMIN")) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("{\"error\":\"You can Update only Your Profile\"}");
		}

		Author updatedAuthor = authorService.updateUser(id, authUpdate);
		AuthorResponseDTO responseDTO = new AuthorResponseDTO(updatedAuthor.getId(), updatedAuthor.getName(),
				updatedAuthor.getEmail(), updatedAuthor.getRole(), updatedAuthor.getAbout());
		return ResponseEntity.ok(responseDTO);
	}

	@GetMapping
	public ResponseEntity<List<AuthorResponseDTO>> getAllUsers() {
		List<Author> authorList = authorService.getAllAuthor();
		List<AuthorResponseDTO> responseDTOList = new ArrayList<>();
		for (Author author : authorList) {
			responseDTOList.add(new AuthorResponseDTO(author.getId(), author.getName(), author.getEmail(),
					author.getRole(), author.getAbout()));
		}
		return ResponseEntity.ok(responseDTOList);
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> deleteUser(@PathVariable Long id,
			@RequestAttribute("currentUserId") Long currentUserId,
			@RequestAttribute("currentUserRole") String currentUserRole){
		if (!id.equals(currentUserId) && !currentUserRole.equals("ADMIN")) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("{\"error\":\"You can Delete only Your Profile\"}");
		}
		authorService.deleteUser(id);
		return ResponseEntity.ok("User Deleted Successfully");
		
	}

}
