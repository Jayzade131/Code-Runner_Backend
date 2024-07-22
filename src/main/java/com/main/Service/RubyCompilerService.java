package com.main.service;

import com.main.factory.CompilerService;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service("rubyCompilerService")
public class RubyCompilerService implements CompilerService {
    private static final String CODE_FILE_NAME = "script.rb";
    @Override
    public String executeAndRun(String code) throws Exception {
        Path codePath = null;
        try {
            // Save the code to a file
            codePath = Paths.get(CODE_FILE_NAME);
            Files.write(codePath, code.getBytes());

            // Execute the Ruby code
            ProcessBuilder processBuilder = new ProcessBuilder("C:\\Ruby32-x64\\bin\\ruby.exe", CODE_FILE_NAME);
            processBuilder.redirectErrorStream(true); // Combine stdout and stderr
            Process runProcess = processBuilder.start();
            int runExitCode = runProcess.waitFor();

            // Capture output
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
}
