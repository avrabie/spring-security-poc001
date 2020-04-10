package ai.sper.helloworld001.controllers.handlers;

import ai.sper.helloworld001.models.LoginRequest;
import ai.sper.helloworld001.models.LoginResponse;
import ai.sper.helloworld001.repos.UserService;
import ai.sper.helloworld001.services.JwtTokenUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
public class AuthHandler {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;

    public AuthHandler(UserService userService, PasswordEncoder passwordEncoder, JwtTokenUtil jwtTokenUtil) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    public Mono<ServerResponse> login(ServerRequest request) {
        Mono<LoginRequest> loginRequest = request.bodyToMono(LoginRequest.class);
        return loginRequest.flatMap(login -> userService.findByUsername(login.getUsername())
                .flatMap(user -> {
                    if (passwordEncoder.matches(login.getPassword(), user.getPassword())) {
                        return ServerResponse.ok()
                                .contentType(APPLICATION_JSON)
//                                .body(BodyInserters.fromObject(new LoginResponse(tokenProvider.generateToken(user))))
                                .body(BodyInserters.fromObject(new LoginResponse(jwtTokenUtil.generateToken(user))))
//                                .body(BodyInserters.fromValue("YEs It matches!!!"))
                                ;

                    } else {
                        return ServerResponse.badRequest()
                                .body(BodyInserters.fromValue("Invalid Password!"));
                    }
                })
                .switchIfEmpty(ServerResponse.badRequest()
//                        .body(BodyInserters.fromObject(new ApiResponse(400, "User does not exist", null)))
                        .body(BodyInserters.fromObject(new LoginResponse(jwtTokenUtil.generateToken("Vania")))
                )));
    }

    public Mono<ServerResponse> logout(ServerRequest serverRequest) {
        return ServerResponse.ok()
                .body(BodyInserters.fromValue("This should be a logout implementation"));
    }

}
