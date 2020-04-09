package ai.sper.helloworld001;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.config.EnableWebFlux;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebFlux;


@SpringBootApplication
@EnableWebFlux
@EnableSwagger2WebFlux
public class HelloWorld001Application {

    public static void main(String[] args) {
        SpringApplication.run(HelloWorld001Application.class, args);
    }

}


