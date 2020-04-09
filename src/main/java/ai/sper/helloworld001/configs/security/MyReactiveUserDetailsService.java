package ai.sper.helloworld001.configs.security;

import ai.sper.helloworld001.repos.UserService;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import reactor.core.publisher.Mono;

public class MyReactiveUserDetailsService implements ReactiveUserDetailsService {


    private final UserService userService;

    public MyReactiveUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return userService.findByUsername(username);
    }
}
