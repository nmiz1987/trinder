package iob.rest;

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import iob.boundaries.NewUserBoundary;
import iob.boundaries.UserBoundary;
import iob.logic.users.UsersService;

@RestController
public class UserController {
	private UsersService user;

	@Autowired
	public UserController(UsersService user) {
		this.user = user;
	}

	private static boolean patternMatches(String emailAddress) {
		String regexPattern = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
		return Pattern.compile(regexPattern).matcher(emailAddress).matches();
	}

	// Create a new user
	@RequestMapping(path = "/iob/users", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)

	public UserBoundary createUser(@RequestBody NewUserBoundary input) {

		// Email validation (Example: username@domain.com)
		if (!patternMatches(input.getEmail()))
			throw new RuntimeException("Email not valid.");
		else
			return this.user.createUser(input);
	}

	// Login valid user and retrieve user details
	@RequestMapping(path = "/iob/users/login/{userDomain}/{userEmail}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public UserBoundary login(@PathVariable("userDomain") String domain, @PathVariable("userEmail") String email) {
		UserBoundary userLogIn = this.user.login(domain, email);
		return userLogIn;
	}

	// Update user details
	@RequestMapping(path = "/iob/users/{userDomain}/{userEmail}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public void updateUser(@PathVariable("userDomain") String domain, @PathVariable("userEmail") String email,
			@RequestBody UserBoundary input) {
		this.user.updateUser(domain, email, input);
	}
}
