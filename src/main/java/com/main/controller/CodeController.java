package com.main.controller;



import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.main.Service.CodeCompilerService;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
@Slf4j
public class CodeController {
	
	@Autowired
    private CodeCompilerService codeCompilerService;


	
	@PostMapping(value="/java/compile" ,consumes = "text/plain")
    public  ResponseEntity<String> compileAndRun(@RequestBody String code) {
        try {
            System.out.println(code);
            log.info("request received...!");
            String output = codeCompilerService.executeJavaCode(code);
            return ResponseEntity.ok(output);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error:\n" + e.getMessage());
        }
    }

}
