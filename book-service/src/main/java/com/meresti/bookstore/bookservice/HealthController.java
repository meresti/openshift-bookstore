package com.meresti.bookstore.bookservice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("health-2")
public class HealthController {

    @GetMapping
    public ResponseEntity<?> health() {
        return ResponseEntity.ok().build();
    }

}
