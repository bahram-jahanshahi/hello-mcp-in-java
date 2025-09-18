package se.bahram.ai.simple_mcp_client.shells;

import org.springframework.shell.standard.ShellComponent;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.shell.standard.ShellMethod;

import java.util.Arrays;
import java.util.stream.Collectors;

@ShellComponent
public class ChatCommands {
    private final ChatClient chatClient;
    private final ToolCallbackProvider toolProvider;

    public ChatCommands(ChatClient chatClient, ToolCallbackProvider toolProvider) {
        this.chatClient = chatClient;
        this.toolProvider = toolProvider;
    }

    @ShellMethod(key = "ping", value = "Simple sanity check")
    public String ping() { return "pong"; }

    @ShellMethod(key = "tools", value = "List available MCP tools")
    public String tools() {
        return Arrays.stream(toolProvider.getToolCallbacks())
                .map(tc -> {
                    var spec = tc.getToolDefinition();
                    return "- " + spec.name() + ": " + spec.description();
                })
                .collect(Collectors.joining("\n"));
    }

    @ShellMethod(key = "ask", value = "Ask the AI; it may call tools. Example: ask what time is it in Europe/Stockholm and save it to notes/time.txt")
    public String ask(String question) {
        // tools already registered globally via ChatClientConfig; you can also add per-call:
        // .tools(toolProvider.getToolCallbacks())
        return chatClient
                .prompt()
                .user(question)
                //.toolCallbacks(toolProvider.getToolCallbacks())
                .call()
                .content();
    }
}
