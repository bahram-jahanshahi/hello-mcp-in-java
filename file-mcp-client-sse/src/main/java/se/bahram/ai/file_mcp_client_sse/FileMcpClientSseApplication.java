package se.bahram.ai.file_mcp_client_sse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FileMcpClientSseApplication {

	public static void main(String[] args) {
		var app = new SpringApplication(FileMcpClientSseApplication.class);
		app.setWebApplicationType(WebApplicationType.NONE); // CLI
		app.run(args);
	}

}
