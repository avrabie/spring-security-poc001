package ai.sper.helloworld001.controllers.routers;

import ai.sper.helloworld001.controllers.handlers.AuthHandler;
import ai.sper.helloworld001.controllers.handlers.GreetingHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Component
public class GreetingRouter {

    private final GreetingHandler greetingHandler;
    private final AuthHandler authHandler;

    public GreetingRouter(GreetingHandler greetingHandler, AuthHandler authHandler) {
        this.greetingHandler = greetingHandler;
        this.authHandler = authHandler;
    }

    @Bean
    public RouterFunction<ServerResponse> route() {

        return RouterFunctions
                .route(RequestPredicates.GET("/hello").and(RequestPredicates.accept(MediaType.TEXT_PLAIN)), greetingHandler::hello)
                .andRoute(RequestPredicates.POST("/auth/login").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), authHandler::login)
                .andRoute(RequestPredicates.POST("/auth/logout").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), authHandler::logout);
    }
}
