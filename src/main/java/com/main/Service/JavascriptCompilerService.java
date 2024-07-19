package com.main.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
public class JavascriptCompilerService implements CompilerService {
    @Override
    public String executeAndRun(String code) throws IOException {

        Path jsFilePath = null;
        String fileName = null;
        try {
            log.info("this is service ");
            fileName = "TempScript" + UUID.randomUUID().toString().replace("-", "");

            // Write code to a temporary file
            jsFilePath = Paths.get(fileName + ".js");
            Files.write(jsFilePath, code.getBytes());

            // Run the JavaScript code using Node.js
            Process process = Runtime.getRuntime().exec("node " + fileName + ".js");

            // Capture output and error streams
            BufferedReader outputReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            StringBuilder output = new StringBuilder();
            String line;

            while ((line = outputReader.readLine()) != null) {
                output.append(line).append("\n");
            }

            while ((line = errorReader.readLine()) != null) {
                output.append(line).append("\n");
            }

            // Wait for the process to complete
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                output.append("Process exited with error code: ").append(exitCode).append("\n");
            }

            return output.toString();
        } catch (Exception e) {
            return "Exception occurred: " + e.getMessage();
        } finally {
            if (Objects.nonNull(jsFilePath)) {
                Files.deleteIfExists(jsFilePath);
            }
        }
    }


}
