package com.STWN.healthcare_project.controller;

import com.STWN.healthcare_project.common.exception.BadRequestException;
import com.STWN.healthcare_project.common.exception.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @GetMapping("/hello")
    public String helloController(){
        return "hello world";
    }
    @GetMapping("/bad-request")
    public ResponseEntity<String> badRequestError(){
        throw new BadRequestException("ada error bad request");
    }
    @GetMapping("/generic-request")
    public ResponseEntity<String> genericError(){
        throw new RuntimeException("ada error generic request");
    }
    @GetMapping("/not-found")
    public ResponseEntity<String> notFoundError(){
        throw new ResourceNotFoundException("ada error not found request");
    }
}
