package com.main.service;

import com.main.factory.CompilerService;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service("phpCompilerService")
public class PhpCompilerService implements CompilerService {

    private static final String CODE_FILE_NAME = "script.php";


    @Override
    public String executeAndRun(String code) throws IOException {
        Path codePath = null;
        try {
            // Save the code to a file
            codePath = Paths.get(CODE_FILE_NAME);
            Files.write(codePath, code.getBytes());

            // Execute the PHP code
            Process runProcess = new ProcessBuilder("C:\\Program Files\\php\\php.exe", CODE_FILE_NAME).start();
            int runExitCode = runProcess.waitFor();

            if (runExitCode != 0) {
                // Capture runtime errors
                return "Runtime error:\n" + getProcessErrorOutput(runProcess);
            }

            // Capture successful output
            return getProcessOutput(runProcess);
        } catch (Exception e) {
            return "Exception: " + e.getMessage();
        } finally {
            Files.deleteIfExists(codePath);
        }
    }

    private String getProcessOutput(Process process) throws Exception {
        StringBuilder output = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
        }
        return output.toString();
    }

    private String getProcessErrorOutput(Process process) throws Exception {
        StringBuilder output = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
        }
        return output.toString();
    }
}
