package by.tms.web.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI 3 metadata for the REST API, rendered by Swagger UI at {@code /swagger-ui.html}.
 * <p>
 * Authentication is session based: the client calls {@code POST /api/auth/login} and the server
 * replies with the {@code JSESSIONID} cookie, which is then reused on every further request.
 * The cookie is declared as an API key security scheme so that it can be supplied from the UI.
 */
@Configuration
public class OpenApiConfig {

    private static final String SESSION_COOKIE_SCHEME = "sessionCookie";

    @Bean
    public OpenAPI libraryOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("SecondLibProject API")
                        .description("REST API of the library management application. Endpoints under "
                                + "/api/admin/** require the admin authority, /api/librarian/** require "
                                + "librarian or admin, and every other /api/** endpoint requires an "
                                + "authenticated session. Log in through POST /api/auth/login and the "
                                + "returned JSESSIONID cookie is sent automatically on subsequent requests.")
                        .version("2.0.0"))
                .components(new Components().addSecuritySchemes(SESSION_COOKIE_SCHEME,
                        new SecurityScheme()
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.COOKIE)
                                .name("JSESSIONID")
                                .description("Session cookie issued by POST /api/auth/login")));
    }
}
