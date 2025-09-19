package se.bahram.ai.file_mcp_client_sse.shells;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.definition.ToolDefinition;
import org.springframework.boot.CommandLineRunner;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.Arrays;
import java.util.stream.Collectors;

@ShellComponent
public record ChatCommands(ChatClient chatClient, ToolCallbackProvider toolCallbackProvider) {

    @ShellMethod(key = "ping", value = "Sanity check")
    public String ping() { return "pong"; }

    @ShellMethod(key = "tools", value = "List tools discovered from the SSE MCP server")
    public String tools() {
        return Arrays.stream(toolCallbackProvider.getToolCallbacks())
                .map(tc -> {
                    var spec = tc.getToolDefinition();
                    return "- " + spec.name() + ": " + spec.description();
                })
                .collect(Collectors.joining("\n"));
    }

    @ShellMethod(key = "ask", value = "Ask the AI; it may call tools as needed")
    public String ask(String question) {
        return chatClient
                .prompt()
                .user(question)
                // (defaultToolCallbacks are already set; you can re-declare per call if you prefer)
                .call()
                .content();
    }
}
