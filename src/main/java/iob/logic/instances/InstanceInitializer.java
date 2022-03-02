package iob.logic.instances;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Profile("instance-manuel-testing")
public class InstanceInitializer implements CommandLineRunner {
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
		this.url = "http://localhost:" + port + "/iob/instances";
	}

	@Override
	public void run(String... args) throws Exception {
	}
}
