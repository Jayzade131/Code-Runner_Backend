package com.main.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.main.Service.CodeCompilerService;

@RestController
@RequestMapping("/api")
public class CodeController {
	
	@Autowired
    private CodeCompilerService codeCompilerService;
	
	
	@PostMapping(value="/java/compile" ,consumes = "text/plain")
    public  ResponseEntity<String> compileAndRun(@RequestBody String code) {
        try {
            String output = codeCompilerService.executeJavaCode(code);
            return ResponseEntity.ok(output);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error:\n" + e.getMessage());
        }
    }

}
