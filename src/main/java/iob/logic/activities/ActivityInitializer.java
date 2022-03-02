package iob.logic.activities;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Profile("activity-manuel-testing")
public class ActivityInitializer implements CommandLineRunner {
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
		this.url = "http://localhost:" + port + "/iob/activities";
	}

	@Override
	public void run(String... args) throws Exception {
	}
}
