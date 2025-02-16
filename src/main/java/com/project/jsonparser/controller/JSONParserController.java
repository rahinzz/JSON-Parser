package com.project.jsonparser.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.project.jsonparser.utilityJ.JSONParser;

@Controller
@ResponseBody
public class JSONParserController {
	
	@PostMapping("/parseJSON")
	public ResponseEntity<?> parseJSON(@RequestParam("file") MultipartFile file) {
		
		try {
			// Convert file to String
            String jsonContent = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))
                .lines().collect(Collectors.joining("\n"));

            // Pass the JSON string to the parser
            JSONParser parser = new JSONParser(jsonContent);
            Object result = parser.parse();
            
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(result);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}
	
	@GetMapping("/hello")
	public String sayHello() {
		return "Hello";
	}

}
