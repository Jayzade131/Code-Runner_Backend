package com.main.Service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;


@Service
@Slf4j
public class CodeCompilerService {

    public String executeJavaCode(String code) throws IOException {
        try {
            log.info("this is service ");
            String regex = "(class\\s+\\w+\\s*\\{)";
            String fileName = "TempClasss" + UUID.randomUUID().toString().replace("-", "");

            code = code.replaceAll(regex, " " + "class " + fileName + " {");
            // Generate a unique class name or use a fixed one if the code is predefined


            // Write code to a temporary file
            Path javaFilePath = Paths.get(fileName + ".java");
            Files.write(javaFilePath, code.getBytes());

            // Compile the Java code
            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
            ByteArrayOutputStream compileOutput = new ByteArrayOutputStream();
            int compilationResult = compiler.run(null, compileOutput, compileOutput, fileName + ".java");

            if (compilationResult != 0) {
                return "Compilation Failed:\n" + compileOutput;
            }


            // Run the compiled Java code
            Process process = Runtime.getRuntime().exec("java " + fileName);

            // Capture output
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            // Delete the temporary .java and .class files
            Files.deleteIfExists(javaFilePath);
            Files.deleteIfExists(Paths.get(fileName + ".class"));

            return output.toString();
        } catch (Exception e) {
           return e.getMessage();
        }
    }


}
