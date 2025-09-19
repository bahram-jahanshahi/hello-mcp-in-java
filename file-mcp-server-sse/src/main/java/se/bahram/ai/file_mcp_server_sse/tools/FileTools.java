package se.bahram.ai.file_mcp_server_sse.tools;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

import java.nio.file.*;


@Service
public class FileTools {

    private Path baseDir() {
        // A safe sandbox under user home; adjust if you like
        return Paths.get(System.getProperty("user.home"), "mcp-data")
                .toAbsolutePath().normalize();
    }

    private Path safeResolve(String rel) {
        Path base = baseDir();
        Path resolved = base.resolve(rel).normalize();
        if (!resolved.startsWith(base)) {
            throw new IllegalArgumentException("Access denied: outside base directory");
        }
        return resolved;
    }

    @Tool(description = "Read a UTF-8 text file relative to the server's safe folder (~/mcp-data). 'path' is relative and must not contain '..'.")
    public String readFile(String path) {
        try {
            Path p = safeResolve(path);
            if (!Files.exists(p)) return "(not found) " + p;
            return Files.readString(p);
        } catch (Exception e) {
            throw new RuntimeException("readFile failed: " + e.getMessage(), e);
        }
    }

    @Tool(description = "Write a UTF-8 text file under the server's safe folder (~/mcp-data). Creates parent folders as needed.")
    public String writeFile(String path, String content) {
        try {
            Path p = safeResolve(path);
            Files.createDirectories(p.getParent());
            Files.writeString(p, content, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            return "Wrote " + p.toString();
        } catch (Exception e) {
            throw new RuntimeException("writeFile failed: " + e.getMessage(), e);
        }
    }
}
