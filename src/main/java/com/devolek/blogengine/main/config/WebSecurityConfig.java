package com.devolek.blogengine.main.config;


import com.devolek.blogengine.main.security.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsServiceImpl userService;

    public WebSecurityConfig(UserDetailsServiceImpl userService) {
        this.userService = userService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .cors()
                .and()
                .authorizeRequests()
                //Доступ только для не зарегистрированных пользователей
                .antMatchers("/api/auth/**", "/", "/static/**", "/css/**",
                        "/img/**", "/js/**", "/imgs/**", "/fonts/**",
                        "/api/post/**", "/favicon.ico", "/api/init",
                        "/api/settings", "/api/tag", "/api/calendar", "/login/**", "/posts/**"
                        , "/calendar/*", "/post/*", "/stat").permitAll()
                .antMatchers("/api/post/my", "/api/image", "/api/comment").hasRole("USER")
                .antMatchers("/api/moderation").hasRole("MODERATOR")
                //Все остальные страницы требуют аутентификации
                .anyRequest().authenticated();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
    }
}
