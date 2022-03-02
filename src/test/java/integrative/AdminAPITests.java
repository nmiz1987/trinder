package integrative;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;

import iob.boundaries.NewUserBoundary;
import iob.boundaries.UserBoundary;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class AdminAPITests {
	private RestTemplate restTemplate;
	private String url;
	private String projectName;
	private int port;
	
	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
	}
	
	@PostConstruct
	public void init() {
		this.restTemplate = new RestTemplate();
		this.url = "http://localhost:" + this.port + "/iob";
	}
	
	@AfterEach
	public void tearDown() {
		this.restTemplate
			.delete(this.url);
	}
	
	@Value("${spring.application.name}")
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	
	@Test
	@DisplayName("Test Pagination: export all users in the domain")
	public void testGetAllUsersUsingPagination() throws Exception{
		
		// GIVEN: server contains 6 users(admin, manager, 4 user)
		List<UserBoundary> addedUsers = new ArrayList<>();
		
		UserBoundary adminUser = this.restTemplate
			.postForObject(this.url+"/users", 
							new NewUserBoundary("aAdmin@gmail.com", "netanel", "ADMIN", "AAA"), 
							UserBoundary.class);
		addedUsers.add(adminUser);
		
		UserBoundary managerUser = this.restTemplate
			.postForObject(this.url+"/users", 
							new NewUserBoundary("bManager@gmail.com", "aviv", "MANAGER", "BBB"), 
							UserBoundary.class);
		addedUsers.add(managerUser);
		
		UserBoundary player1User = this.restTemplate
			.postForObject(this.url+"/users", 
							new NewUserBoundary("cPlayer1@gmail.com", "gabi", "PLAYER", "CCC"), 
							UserBoundary.class);
		addedUsers.add(player1User);
		
		UserBoundary player2User = this.restTemplate
				.postForObject(this.url+"/users", 
							new NewUserBoundary("dPlayer2@gmail.com", "shem", "PLAYER", "DDD"), 
							UserBoundary.class);
		addedUsers.add(player2User);
		
		UserBoundary player3User = this.restTemplate
				.postForObject(this.url+"/users", 
							new NewUserBoundary("ePlayer3@gmail.com", "adi", "PLAYER", "EEE"), 
							UserBoundary.class);
		addedUsers.add(player3User);
		
		UserBoundary player4User = this.restTemplate
				.postForObject(this.url+"/users", 
							new NewUserBoundary("fPlayer4@gmail.com", "dani", "PLAYER", "FFF"), 
							UserBoundary.class);
		addedUsers.add(player4User);
		
		// WHEN: the user requesting is an adim(role) & I get all messages in page: 0 , of size: 5
		String adminDomain = projectName;
		String adminEmail = "aAdmin@gmail.com";
		int size = 5;
		int page = 0;
		UserBoundary[] results = 
				this.restTemplate.getForObject(this.url+"/admin/users/{adminDomain}/{adminEmail}"
												+ "?size={size}&page={page}", 
												UserBoundary[].class, adminDomain, adminEmail, size, page);
		
		// THEN: get 5 users and not 6
		assertThat(results).hasSize(size).usingRecursiveFieldByFieldElementComparator().isSubsetOf(addedUsers);

	}
	

}
