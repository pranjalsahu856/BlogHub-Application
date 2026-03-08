package in.bloghub.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.bloghub.dto.AuthorUpdateDTO;
import in.bloghub.entity.Author;
import in.bloghub.exception.ResourceNotFoundException;
import in.bloghub.repository.AuthorRepository;

@Service
public class AuthorService {
	private AuthorRepository authorRepository;

	@Autowired
	public AuthorService(AuthorRepository authorRepository) {
		this.authorRepository = authorRepository;
	}
	
	public List<Author> getAllAuthor(){
		return authorRepository.findAll();
	}
	
	public Author getUserById(Long id) {
		Author author = authorRepository.findById(id).orElse(null);
		if(author==null) {
			throw new ResourceNotFoundException("No author with id : "+id +" Found");
		}
		return author;
	}
	
	public Author updateUser(Long id, AuthorUpdateDTO updAuthor) {
		Author author = getUserById(id);
		if(updAuthor.getName()==null && updAuthor.getEmail()==null && updAuthor.getAbout()==null) {
			throw new RuntimeException("Empty Objects not Allowed");
		}
		if(updAuthor.getName()!=null && updAuthor.getName().isBlank()) {
			throw new RuntimeException("Name can not be Blank");
		}
		if(updAuthor.getAbout()!=null && updAuthor.getAbout().isBlank()) {
			throw new RuntimeException("About can not be Blank");
		}
		if(updAuthor.getName()!=null) {
			author.setName(updAuthor.getName());
		}
		if(updAuthor.getEmail()!=null) {
			author.setEmail(updAuthor.getEmail());
		}
		if(updAuthor.getAbout()!=null) {
			author.setAbout(updAuthor.getAbout());
		}
		return authorRepository.save(author);
	}
	
	public void deleteUser(Long id) {
		Author author = getUserById(id);
		authorRepository.delete(author);
	}
}
