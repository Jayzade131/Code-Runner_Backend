package com.main.factory;

import com.main.constant.Langauge;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;



@Component
@RequiredArgsConstructor
public class CompilerContext {
    @Qualifier("javaCompilerService")
    private final CompilerService javaCompilerService;

    @Qualifier("pythonCompilerService")
    private final CompilerService pythonCompilerService;

    @Qualifier("javascriptCompilerService")
    private final CompilerService javascriptCompilerService;

    @Qualifier("typescriptCompilerService")
    private final CompilerService typescriptCompilerService;

    @Qualifier("cppCompilerService")
    private final CompilerService cppCompilerService;

    @Qualifier("cCompilerService")
    private final CompilerService cCompilerService;

    public String compileAndRun(String compiler, String code) throws Exception {
        return getCompiler(compiler).executeAndRun(code);
    }


    private CompilerService getCompiler(String compiler) {

        if (compiler.equals(Langauge.JAVA.toString())) {
            return javaCompilerService;

        } else if (compiler.equals(Langauge.PYTHON.toString())) {
            return pythonCompilerService;

        } else if (compiler.equals(Langauge.JAVASCRIPT.toString())) {
            return javascriptCompilerService;

        } else if (compiler.equals(Langauge.TYPESCRIPT.toString())) {
            return typescriptCompilerService;

        } else if (compiler.equals(Langauge.CPP.toString())) {
            return cppCompilerService;

        } else if (compiler.equals(Langauge.C.toString())) {
            return cCompilerService;
        }
        throw new RuntimeException("Invalid language or compiler not implemented for language " + compiler);
    }
}
