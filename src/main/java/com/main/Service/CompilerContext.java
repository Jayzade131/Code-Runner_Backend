package com.main.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CompilerContext {

    private final CompilerService javaCompilerService;
    private final CompilerService pythonCompilerService;
    private final CompilerService javascriptCompilerService;
    private final CompilerService typescriptCompilerService;

    public String compileAndRun(String compiler, String code) throws IOException {
        return getCompiler(compiler).executeAndRun(code);
    }


    private CompilerService getCompiler(String compiler) {
        if (compiler.equals("JAVA")) {
            return javaCompilerService;
        } else if (compiler.equals("PYTHON")) {
            return pythonCompilerService;

        } else if (compiler.equals("JAVASCRIPT")) {
            return javascriptCompilerService;
        } else if (compiler.equals("TYPESCRIPT")) {
            return typescriptCompilerService;

        }
        throw new RuntimeException("Invalid language or compiler not implemented for language " + compiler);
    }
}
