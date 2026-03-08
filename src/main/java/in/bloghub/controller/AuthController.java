package in.bloghub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.bloghub.dto.AuthResponseDTO;
import in.bloghub.dto.LoginRequestDTO;
import in.bloghub.dto.RegisterRequestDTO;
import in.bloghub.service.AuthService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	private AuthService authService;

	@Autowired
	public AuthController(AuthService authService) {
		this.authService = authService;
	}
	
	@PostMapping("/register")
	public ResponseEntity<AuthResponseDTO> register(@RequestBody @Valid RegisterRequestDTO request){
		AuthResponseDTO authDTO = authService.register(request);
		return new ResponseEntity<>(authDTO,HttpStatus.CREATED);
	}
	
	@PostMapping("/login")
	public ResponseEntity<AuthResponseDTO> login(@RequestBody @Valid LoginRequestDTO request,HttpSession session){
		AuthResponseDTO authDTO = authService.login(request,session);
		return ResponseEntity.ok(authDTO);
	}
	
	@PostMapping("/logout")
	public ResponseEntity<String> logout(HttpSession session){
		authService.logout(session);
		return ResponseEntity.ok("Logged out Successfully");
	}
	
	@GetMapping("/me")
	public ResponseEntity<AuthResponseDTO> getCurrentUser(HttpSession session){
		AuthResponseDTO authDTO = authService.getCurrentUser(session);
		return ResponseEntity.ok(authDTO);
	}

}
