package se.bahram.ai.file_mcp_server_sse.tools;

import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ToolsProvider {

    @Bean
    ToolCallbackProvider toolCallbackProvider(FileTools tools) {
        return MethodToolCallbackProvider
                .builder()
                .toolObjects(tools)
                .build();
    }
}
