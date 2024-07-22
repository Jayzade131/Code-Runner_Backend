package com.main.service;

import com.main.factory.CompilerService;
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

@Service("typescriptCompilerService")
public class TypescriptCompilerService implements CompilerService {


    public String executeAndRun(String code) throws  IOException {

        Path tsFilePath = null;
        Path jsFilePath = null;
        String tsFileName = null;
        String jsFileName = null;

        try {
            tsFileName = "Tempscript" + UUID.randomUUID().toString().replace("-", "");


            // Write TypeScript code to a temporary file
            tsFilePath = Paths.get(tsFileName + ".ts");
            Files.write(tsFilePath, code.getBytes());

            // Transpile TypeScript to JavaScript
            Process tscProcess = Runtime.getRuntime().exec("tsc " + tsFileName + ".ts");
            int tscExitCode = tscProcess.waitFor();
            if (tscExitCode != 0) {
                BufferedReader errorReader = new BufferedReader(new InputStreamReader(tscProcess.getErrorStream()));
                StringBuilder errorOutput = new StringBuilder();
                String line;
                while ((line = errorReader.readLine()) != null) {
                    errorOutput.append(line).append("\n");
                }
                return "TypeScript compilation failed:\n" + errorOutput.toString();
            }
            jsFileName = tsFileName;
            // Run the transpiled JavaScript code using Node.js
            jsFilePath = Paths.get(jsFileName + ".js");
            Process nodeProcess = Runtime.getRuntime().exec("node " + jsFileName + ".js");

            // Capture output and error streams
            BufferedReader outputReader = new BufferedReader(new InputStreamReader(nodeProcess.getInputStream()));
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(nodeProcess.getErrorStream()));
            StringBuilder output = new StringBuilder();
            String line;

            while ((line = outputReader.readLine()) != null) {
                output.append(line).append("\n");
            }

            while ((line = errorReader.readLine()) != null) {
                output.append(line).append("\n");
            }

            // Wait for the process to complete
            int exitCode = nodeProcess.waitFor();
            if (exitCode != 0) {
                output.append("Process exited with error code: ").append(exitCode).append("\n");
            }

            return output.toString();
        } catch (Exception e) {
            return "Exception occurred: " + e.getMessage();
        } finally {
            if (Objects.nonNull(tsFilePath)) {
                Files.deleteIfExists(tsFilePath);
            }
            if (Objects.nonNull(jsFilePath)) {
                Files.deleteIfExists(jsFilePath);
            }
        }
    }


}
