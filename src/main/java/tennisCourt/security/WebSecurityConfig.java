package tennisCourt.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import tennisCourt.model.Client;
import tennisCourt.model.User;
import tennisCourt.repo.ClientRepository;
import tennisCourt.repo.UserRepository;
import tennisCourt.service.UserService;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private UserService userDetailsService;
    private UserRepository userRepository;
    private ClientRepository clientRepository;

    @Autowired
    public WebSecurityConfig(UserService userDetailsService, UserRepository userRepository, ClientRepository clientRepository) {
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
        this.clientRepository = clientRepository;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/OurTennis/**").hasRole("USER")
                .and()
                .formLogin().permitAll().defaultSuccessUrl("/OurTennis", true)
                .and()
                .logout()
                .logoutSuccessUrl("/ourTennis")
                .permitAll();
    }

    @Bean
    public static PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}

