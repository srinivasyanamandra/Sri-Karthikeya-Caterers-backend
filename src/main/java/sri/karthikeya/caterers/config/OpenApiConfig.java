package sri.karthikeya.caterers.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Sri Karthikeya Caterers API")
                        .version("1.0")
                        .description("Production-grade REST API for catering business management")
                        .contact(new Contact()
                                .name("Sri Karthikeya Caterers")
                                .email("info@srikarthikeyacaterers.com")))
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Local Server"),
                        new Server().url("https://api.srikarthikeyacaterers.com").description("Production Server")
                ));
    }
}
