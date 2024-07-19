package com.main.controller;

import com.main.dto.ResponseDto;
import com.main.service.CompilerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
@Slf4j
public class CompilerController {

    @Autowired
    private CompilerContext compilerContext;

    @PostMapping(value="/compile/{lang}" ,consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDto> compileAndRun(@PathVariable("lang") String lang, @RequestBody String code) {
        try {
            System.out.println(code);
            log.info("request received...!");
            String output = compilerContext.compileAndRun(lang,code);
            return ResponseEntity.ok(ResponseDto.OK(output));
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            return ResponseEntity.status(500).body(ResponseDto.ERROR("Error:\n" + e.getMessage()));
        }
    }
}
