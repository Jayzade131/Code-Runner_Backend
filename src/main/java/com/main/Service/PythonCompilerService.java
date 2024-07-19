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
public class PythonCompilerService implements CompilerService {

    public String executeAndRun(String code) throws IOException {

        Path pythonFilePath = null;
        String fileName = null;
        try {

            fileName = "Temp" + UUID.randomUUID().toString().replace("-", "");

            // Write code to a temporary file
            pythonFilePath = Paths.get(fileName + ".py");
            Files.write(pythonFilePath, code.getBytes());

            // Run the Python code using Python interpreter
            Process process = Runtime.getRuntime().exec("python " + fileName + ".py");
            try( BufferedReader outputReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                 BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));){
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
            }catch (InterruptedException e){
                return "Exception occurred: " + e.getMessage();
            }

            // Capture output and error streams



        } catch (Exception e) {
            return "Exception occurred: " + e.getMessage();
        } finally {
            if (Objects.nonNull(pythonFilePath)) {
               Files.deleteIfExists(Paths.get(fileName + ".py"));
           }
        }

    }


}
