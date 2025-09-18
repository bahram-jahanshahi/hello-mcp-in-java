package se.bahram.ai.simple_mcp_client.configs;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.stream.Collectors;

@Configuration
public class OpenAiConfig {

    @Bean
    ChatClient chatClient(ChatClient.Builder builder, ToolCallbackProvider tools) {
        String toolCatalog = Arrays.stream(tools.getToolCallbacks())
                .map(tc -> {
                    var spec = tc.getToolDefinition();
                    return "- " + spec.name() + ": " + spec.description();
                })
                .collect(Collectors.joining("\n"));

        String system = """
            You are a helpful assistant with access to MCP tools.
            Available tools (name: description):
            %s

            Plan whether tools are needed; if yes, call them with precise inputs.
            For file I/O, prefer readFile/writeFile under the safe base.
            Keep final answers concise; summarize tool results for the user.
            """.formatted(toolCatalog);

        return builder
                .defaultSystem(system)
                .defaultToolCallbacks(tools.getToolCallbacks())
                .build();
    }
}
