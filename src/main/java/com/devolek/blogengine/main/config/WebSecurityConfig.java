package com.devolek.blogengine.main.config;


import com.devolek.blogengine.main.security.jwt.JwtConfigurer;
import com.devolek.blogengine.main.security.jwt.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableGlobalMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtTokenProvider jwtTokenProvider;

    public WebSecurityConfig(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
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
                .antMatcher("/api/**")
                .httpBasic().disable()
                .csrf().disable()
                .cors()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                //Доступ только для не зарегистрированных пользователей
                .antMatchers("/api/auth/**",
                        "/api/calendar",
                        "/api/post/**",
                        "/api/init",
                        "/api/settings",
                        "/api/tag",
                        "/api/statistics/all").permitAll()
                .antMatchers("/api/post/my",
                        "/api/statistics/my",
                        "/api/image",
                        "/api/comment/",
                        "/api/profile/my").hasRole("USER")
                .antMatchers("/api/post/moderation", "/api/moderation").hasRole("MODERATOR")
                .anyRequest().authenticated()
                .and()
                .apply(new JwtConfigurer(jwtTokenProvider))
                .and().exceptionHandling().authenticationEntryPoint(Http401EntryPoint.unauthorizedHandler());
    }

}
