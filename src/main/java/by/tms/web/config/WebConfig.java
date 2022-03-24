package by.tms.web.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ComponentScan("by.tms.web")
@EnableWebMvc
@Import({ThymeleafConfig.class, InternationalizationConfig.class})
public class WebConfig implements WebMvcConfigurer {
}
