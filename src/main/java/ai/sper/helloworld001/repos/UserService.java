package ai.sper.helloworld001.repos;

import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@Service
public class UserService {

    private List<UserDetails> userDetails;

    private UserService(PasswordEncoder passwordEncoder) {

        UserDetails user1 = User
                .withUsername("user")
                .password(passwordEncoder.encode("password"))
//                .roles("USER")
                .authorities("ROLE_INNA", "ROLE_USER")
                .build();
        UserDetails user2 = User
                .withUsername("company")
                .password(passwordEncoder.encode("password"))
                .roles("COMPANY")
                .build();
        UserDetails user3 = User
                .withUsername("admin")
                .password(passwordEncoder.encode("password"))
                .roles("ADMIN", "SUPER_ADMIN", "USER")
                .build();
        this.userDetails = Arrays.asList(user1, user2, user3);
    }


    Flux<UserDetails> getAllUsers() {
        return Flux.fromIterable(userDetails);
    }


    public Mono<UserDetails> findByUsername(String username) {
        return Flux.fromIterable(userDetails)
                .filter(user -> user.getUsername().equals(username))
                .next();

    }
}
