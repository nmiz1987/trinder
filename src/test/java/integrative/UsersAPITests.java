package integrative;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.annotation.PostConstruct;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import iob.boundaries.NewUserBoundary;
import iob.boundaries.UserBoundary;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class UsersAPITests {
	private RestTemplate restTemplate;
	private String url;
	private int port;
	
	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
	}
	
	@PostConstruct
	public void init() {
		this.restTemplate = new RestTemplate();
		this.url = "http://localhost:" + this.port + "/iob/users";
	}
	
	@Test
	public void testContext() {
	}
	
//	@BeforeEach
	@AfterEach
	public void setup() {
		this.restTemplate
			.delete(this.url);
	}

	
	@Test
	@DisplayName("Test that create new user")
	public void testCreateANewUser() throws Exception{
		UserBoundary existingUser = this.restTemplate
			.postForObject(this.url, new NewUserBoundary("a@gmail.com", "netanel", "PLAYER", "J"), UserBoundary.class);
		
			System.err.println(existingUser);
			
//		assertThat(existingUser)
//		.isExactlyInstanceOf(NewUserBoundary.class)
//		.isNotNull();
	}
	
	@Test
	@DisplayName("Test Login valid user and retrieve user details")
	public void testLoginValidUserAndRetrieveUserDetails() throws Exception{
		
			UserBoundary existingUser = this.restTemplate
					.getForObject(this.url + "users/login/{userDomain}/{userEmail}",
					UserBoundary.class, "userDomain", "userEmail");
		
//		assertThat(existingUser)
//		.hasSize(1);
		// TODO check if the userName and userEmail in the respond
	}
	
	@Test
	@DisplayName("Test Update User Details")
	public void testUpdateUserDetails() throws Exception{
		UserBoundary existingUser = new UserBoundary();
		existingUser = this.restTemplate.postForObject(this.url, new HashMap<>(), UserBoundary.class);

		UserBoundary update = new UserBoundary();
		
		update.setUsername("New Name");
		this.restTemplate
			.put(this.url + "/{userDomain}/{userEmail}", update
					, existingUser.getUserId().getDomain(), existingUser.getUserId().getEmail());
		
		existingUser.setUsername(update.getUsername());
		
		
		assertThat(this.restTemplate
				.getForObject(this.url + "users/login/{userDomain}/{userEmail}", UserBoundary.class,
						existingUser.getUserId().getDomain(), existingUser.getUserId().getEmail()))
			.usingRecursiveComparison()
			.isEqualTo(existingUser);
		// TODO check if the name as changed
				
	}


}
