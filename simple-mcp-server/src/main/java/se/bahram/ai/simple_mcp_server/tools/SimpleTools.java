package se.bahram.ai.simple_mcp_server.tools;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

import java.nio.file.*;
import java.time.*;
import java.util.*;
import java.net.http.*;
import java.net.*;

@Service
public class SimpleTools {

    @Tool(description = "Do arithmetic. Supported ops: add, sub, mul, div. Example: op='add', a=2, b=3")
    public double calculator(String op, double a, double b) {
        return switch (op) {
            case "add" -> a + b;
            case "sub" -> a - b;
            case "mul" -> a * b;
            case "div" -> b == 0 ? Double.NaN : a / b;
            default -> throw new IllegalArgumentException("Unknown op: " + op);
        };
    }

    @Tool(description = "Return current time in a given IANA timezone, e.g. 'Europe/Stockholm'")
    public String now(String timezone) {
        var zone = ZoneId.of(timezone);
        return ZonedDateTime.now(zone).toString();
    }

    @Tool(description = "Uppercase transformation. Returns the uppercased string.")
    public String toUpper(String text) {
        return text == null ? "" : text.toUpperCase();
    }

    @Tool(description = "Read a UTF-8 text file under a safe base folder. 'path' is relative (no ..)")
    public String readFile(String path) {
        Path base = Paths.get(System.getProperty("user.home"), "mcp-data").toAbsolutePath().normalize();
        Path resolved = base.resolve(path).normalize();
        if (!resolved.startsWith(base)) throw new IllegalArgumentException("Access denied");
        try { return Files.readString(resolved); } catch (Exception e) { throw new RuntimeException(e); }
    }

    @Tool(description = "Write a UTF-8 text file under a safe base folder. Creates parent dirs if needed.")
    public String writeFile(String path, String content) {
        Path base = Paths.get(System.getProperty("user.home"), "mcp-data").toAbsolutePath().normalize();
        Path resolved = base.resolve(path).normalize();
        if (!resolved.startsWith(base)) throw new IllegalArgumentException("Access denied");
        try {
            Files.createDirectories(resolved.getParent());
            Files.writeString(resolved, content, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            return "Wrote " + resolved;
        } catch (Exception e) { throw new RuntimeException(e); }
    }

    @Tool(description = "HTTP GET fetch. Returns body as text. Use for simple public endpoints.")
    public String httpGet(String url) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            var req = HttpRequest.newBuilder(URI.create(url)).GET().build();
            return client.send(req, HttpResponse.BodyHandlers.ofString()).body();
        } catch (Exception e) { throw new RuntimeException(e); }
    }

}
