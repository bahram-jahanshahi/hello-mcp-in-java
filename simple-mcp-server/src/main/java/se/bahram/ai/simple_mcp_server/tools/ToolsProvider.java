package se.bahram.ai.simple_mcp_server.tools;

import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ToolsProvider {

    @Bean
    ToolCallbackProvider toolCallbackProvider(SimpleTools tools) {
        return MethodToolCallbackProvider
                .builder()
                .toolObjects(tools)
                .build();
    }
}
