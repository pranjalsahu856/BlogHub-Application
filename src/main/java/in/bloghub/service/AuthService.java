package in.bloghub.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.bloghub.dto.AuthResponseDTO;
import in.bloghub.dto.LoginRequestDTO;
import in.bloghub.dto.RegisterRequestDTO;
import in.bloghub.entity.Author;
import in.bloghub.exception.ResourceAlreadyExistsException;
import in.bloghub.exception.ResourceNotFoundException;
import in.bloghub.repository.AuthorRepository;
import jakarta.servlet.http.HttpSession;

@Service
public class AuthService {
	private AuthorRepository authorRepository;

	@Autowired
	public AuthService(AuthorRepository authorRepository) {
		this.authorRepository = authorRepository;
	}

	public AuthResponseDTO register(RegisterRequestDTO req) {
		if (authorRepository.existsByEmail(req.getEmail())) {
			throw new ResourceAlreadyExistsException("Email Already Registered");
		}
		Author author = new Author();
		author.setName(req.getName());
		author.setEmail(req.getEmail());
		author.setPassword(req.getPassword());
		author.setAbout(req.getAbout());
		author.setRole("USER");

		Author savedAuthor = authorRepository.save(author);

		return new AuthResponseDTO(savedAuthor.getId(), savedAuthor.getName(), savedAuthor.getEmail(),
				savedAuthor.getRole(), "Registration Successfull");
	}

	public AuthResponseDTO login(LoginRequestDTO req, HttpSession session) {
		Author author = authorRepository.findByEmail(req.getEmail()).orElse(null);
		if (author == null) {
			throw new ResourceNotFoundException("Invalid UserId or Password");
		}
		if (!author.getPassword().equals(req.getPassword())) {
			throw new ResourceNotFoundException("Invalid UserId or Password");
		}
		session.setAttribute("userId", author.getId());
		session.setAttribute("userRole", author.getRole());
		session.setAttribute("userName", author.getName());
		session.setAttribute("userEmail", author.getEmail());

		return new AuthResponseDTO(author.getId(), author.getName(), author.getEmail(), author.getRole(),
				"Login Successfull");
	}

	public void logout(HttpSession session) {
		session.invalidate();
	}

	public AuthResponseDTO getCurrentUser(HttpSession session) {
		Long userId = (Long)session.getAttribute("userId");
		if(userId ==null) {
			throw new ResourceNotFoundException("No user Logged in");
		}
		String userName = (String) session.getAttribute("userName");
		String userEmail = (String)session.getAttribute("userEmail");
		String userRole = (String)session.getAttribute("userRole");
		return new AuthResponseDTO(userId,userName,userEmail,userRole,"Current User Data");
	}

}