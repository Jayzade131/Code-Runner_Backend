package com.main.service;

import com.main.factory.CompilerService;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service("cCompilerService")
public class CCompilerService implements CompilerService {

    private static final String CODE_FILE_NAME = "Main.c";
    private static final String OUTPUT_FILE_NAME = "a.exe";

    @Override
    public String executeAndRun(String code) throws Exception {
        Path codePath = null;
        try {
            // Save the code to a file
            codePath = Paths.get(CODE_FILE_NAME);
            Files.write(codePath, code.getBytes());

            // Compile the code
            Process compileProcess = new ProcessBuilder("gcc", CODE_FILE_NAME, "-o", OUTPUT_FILE_NAME).start();
            int compileExitCode = compileProcess.waitFor();

            if (compileExitCode != 0) {
                // Capture compile-time errors
                return "Compile-time error:\n" + getProcessErrorOutput(compileProcess);
            }

            // Run the compiled code
            Process runProcess = new ProcessBuilder("./" + OUTPUT_FILE_NAME).start();
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
            Files.deleteIfExists(Paths.get(OUTPUT_FILE_NAME));
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