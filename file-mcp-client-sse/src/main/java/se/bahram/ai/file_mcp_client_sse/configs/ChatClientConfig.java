package se.bahram.ai.file_mcp_client_sse.configs;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.stream.Collectors;

@Configuration
public class ChatClientConfig {

    @Bean
    ChatClient chatClient(ChatClient.Builder builder,
                          ToolCallbackProvider mcpTools) {

        String toolCatalog = Arrays.stream(mcpTools.getToolCallbacks())
                .map(tc -> {
                    var spec = tc.getToolDefinition();
                    return "- " + spec.name() + ": " + spec.description();
                })
                .collect(Collectors.joining("\n"));

        String system = """
      You are a helpful assistant with access to MCP tools.

      Available tools:
      %s

      Use tools when they help complete the task.
      For file operations always prefer readFile/writeFile.
      Keep final answers concise; summarize any tool results.
      """.formatted(toolCatalog);

        return builder
                .defaultSystem(system)
                // Register ALL discovered MCP tool callbacks (from SSE server):
                .defaultToolCallbacks(mcpTools.getToolCallbacks())
                .build();
    }
}
