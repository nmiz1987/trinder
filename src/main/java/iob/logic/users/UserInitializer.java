package iob.logic.users;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import iob.boundaries.NewUserBoundary;
import iob.boundaries.UserBoundary;

// to acticate profile, add to properties: spring.profiles.active = default, user-manuel-testing
@Component
@Profile("user-manuel-testing")
public class UserInitializer implements CommandLineRunner {
	private RestTemplate restTemplate;
	private String url;
	private int port;

	@Value("${server.port:8080}")
	public void setPort(int port) {
		this.port = port;
	}

	@PostConstruct
	public void init() {
		this.restTemplate = new RestTemplate();
		this.url = "http://localhost:" + port + "/iob/users";
	}

	@Override
	public void run(String... args) throws Exception {

		// enter the server 6 users(admin, manager, 4 user)
		this.restTemplate.postForObject(this.url, new NewUserBoundary("aAdmin@gmail.com", "netanel", "ADMIN", "AAA"),
				UserBoundary.class);

		this.restTemplate.postForObject(this.url, new NewUserBoundary("bManager@gmail.com", "aviv", "MANAGER", "BBB"),
				UserBoundary.class);

		this.restTemplate.postForObject(this.url, new NewUserBoundary("cPlayer1@gmail.com", "gabi", "PLAYER", "CCC"),
				UserBoundary.class);

		this.restTemplate.postForObject(this.url, new NewUserBoundary("dPlayer2@gmail.com", "shem", "PLAYER", "DDD"),
				UserBoundary.class);

		this.restTemplate.postForObject(this.url, new NewUserBoundary("ePlayer3@gmail.com", "adi", "PLAYER", "EEE"),
				UserBoundary.class);

		this.restTemplate.postForObject(this.url, new NewUserBoundary("fPlayer4@gmail.com", "dani", "PLAYER", "FFF"),
				UserBoundary.class);
	}
}
