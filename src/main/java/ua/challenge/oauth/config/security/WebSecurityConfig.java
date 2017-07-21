package ua.challenge.oauth.config.security;

import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("admin").password("123456").roles("ADMIN");
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /*
        * A quick note here is that the form login configuration isn’t necessary for the Password flow –
        * only for the Implicit flow – so you may be able to skip it depending on what OAuth2 flow you’re using.
        * */

        http
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER)
            .and()
            .csrf().disable()
            .anonymous().disable()
            .authorizeRequests()
                .antMatchers("/oauth/token").permitAll()
                .antMatchers("/oauth/authorize").permitAll()
                .antMatchers("/login").permitAll()
                .anyRequest().authenticated()
            .and()
                .formLogin().permitAll()
        ;
    }
}
