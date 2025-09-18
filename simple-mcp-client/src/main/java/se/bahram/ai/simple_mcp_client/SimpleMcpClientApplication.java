package se.bahram.ai.simple_mcp_client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SimpleMcpClientApplication {

	public static void main(String[] args) {
		var app = new SpringApplication(SimpleMcpClientApplication.class);
		app.setWebApplicationType(WebApplicationType.NONE);
		app.run(args);
	}
}
