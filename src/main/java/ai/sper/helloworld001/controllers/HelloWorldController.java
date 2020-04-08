package ai.sper.helloworld001.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@EnableReactiveMethodSecurity
public class HelloWorldController {

    @GetMapping("/")
    public Mono<String> sayWelcome() {
        return Mono.just("Welcome all!!!");
    }


    @GetMapping("/hello")
    public Mono<String> sayGreeting() {
        return Mono.just("Hello World!!!");
    }

    @GetMapping("/user")
    public Mono<String> thisIsUser() {
        return Mono.just("This is USER!");
    }

    @PreAuthorize("hasRole('ADMIN')")  //this is awesome
    @GetMapping("/admin")
    public Mono<String> sayGreetingAdmin() {
        return Mono.just("Admin realm!");
    }

    @GetMapping("/company")
    public Mono<String> sayCompany() {
        return Mono.just("Company realm!");
    }


}
