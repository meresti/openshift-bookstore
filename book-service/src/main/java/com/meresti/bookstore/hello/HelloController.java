package com.meresti.bookstore.hello;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class HelloController {

    @GetMapping("/message")
    public Mono<String> sayHello(@RequestParam(name = "name", required = false, defaultValue = "World") String name) {
        return Mono.just("Hello " + name + "!");
    }

}
