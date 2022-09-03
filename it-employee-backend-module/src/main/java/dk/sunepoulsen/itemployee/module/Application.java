package dk.sunepoulsen.itemployee.module;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@EnableAsync
@SpringBootApplication( scanBasePackages = {
    "dk.sunepoulsen.tes",
    "dk.sunepoulsen.itemployee.module"
})
public class Application {
    public static void main( String[] args ) {
        SpringApplication.run( Application.class, args );
    }
}
