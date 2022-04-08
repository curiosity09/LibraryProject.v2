package by.tms.web.config;

import by.tms.web.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String TEXT_HTML_CHARSET_UTF_8 = "text/html; charset=UTF-8";
    private final UserDetailsService userDetailsService;

    @Autowired
    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(new EncodingFilterConfig(), ChannelProcessingFilter.class);

        http
                .authorizeRequests()
                .antMatchers("/user/**").hasAnyAuthority(PageUtil.USER_ROLE, PageUtil.ADMIN_ROLE)
                .antMatchers("/librarian/**").hasAnyAuthority(PageUtil.LIBRARIAN_ROLE,
                        PageUtil.ADMIN_ROLE)
                .antMatchers("/admin/**").hasAuthority(PageUtil.ADMIN_ROLE)
                .antMatchers("/resources/**", "/fragments/**",
                        PageUtil.START_PAGE, PageUtil.REGISTER_PAGE).permitAll()
                .anyRequest().permitAll()
                .and()
                .formLogin()
                .loginPage(PageUtil.START_PAGE)
                .defaultSuccessUrl(PageUtil.SUCCESS_PAGE)
                .permitAll()
                .and()
                .logout()
                .invalidateHttpSession(true)
                .logoutSuccessUrl(PageUtil.START_PAGE)
                .permitAll();
    }

    public static class EncodingFilterConfig implements Filter {
        @Override
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
                throws IOException, ServletException {
            request.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.setContentType(TEXT_HTML_CHARSET_UTF_8);
            chain.doFilter(request, response);
        }
    }
}
