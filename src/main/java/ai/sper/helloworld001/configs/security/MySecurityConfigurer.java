package ai.sper.helloworld001.configs.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;


@Configuration
@EnableWebFluxSecurity
public class MySecurityConfigurer {

    @Autowired
    private MyReactiveAuthenticationManager authenticationManager;

    @Autowired
    private MyServerSecurityContextRepository securityContextRepository;


    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        String[] patterns = new String[]{"/auth/**", "/"};
        http.cors().disable()
                .exceptionHandling() // TODO: 4/10/20 this needs to be implemented
                .authenticationEntryPoint((swe, e) -> Mono.fromRunnable(() -> {
                    swe.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED); // TODO: 4/10/20 make some custom responses here
                }))
                .accessDeniedHandler((swe, e) -> Mono.fromRunnable(() -> {
                    swe.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                }))
                .and()
                .csrf().disable() // TODO: 4/10/20 investigate if we can enable it with UI
                .authenticationManager(authenticationManager)
                .securityContextRepository(securityContextRepository)
                .authorizeExchange()
                .pathMatchers(patterns).permitAll()
                .pathMatchers(HttpMethod.OPTIONS).permitAll()
                .anyExchange().authenticated()
        ;


        //v1
//        http
//                .authorizeExchange()
//                .pathMatchers("/").permitAll()
//                .pathMatchers("/auth/**").permitAll()
//                .pathMatchers("/admin").hasAuthority("ROLE_ADMIN")
//                .pathMatchers("/user").hasAuthority("ROLE_INNA")
//                .pathMatchers("/company").hasAnyAuthority("ROLE_USER", "ROLE_COMPANY")
//                .anyExchange().authenticated()
//                .and()
//                .httpBasic()
//                .and()
//                .formLogin()
//                .and()
//                .logout()
//        ;

//        // this should be integrated with tht UI in order to have it enabled
//        http
//                .csrf()
//                .disable();

        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance(); // TODO: 4/10/20 Of course!
//        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}
