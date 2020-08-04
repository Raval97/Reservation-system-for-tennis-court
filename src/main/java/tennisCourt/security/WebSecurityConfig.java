package tennisCourt.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

//    private UserService userDetailsService;
//    private UserRepository userRepository;
//    private ClientRepository clientRepository;
//
//    @Autowired
//    public WebSecurityConfig(UserService userDetailsService, UserRepository userRepository, ClientRepository clientRepository) {
//        this.userDetailsService = userDetailsService;
//        this.userRepository = userRepository;
//        this.clientRepository = clientRepository;
//    }

//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(userDetailsService);
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
//                .antMatchers("/client/All/actual").hasRole("USER")
                .antMatchers("/client/**").hasRole("USER")
                .antMatchers("/admin/**").hasRole("ADMIN")
                .and()
                .formLogin().permitAll().defaultSuccessUrl("/default", true)
                .and()
                .logout()
                .logoutSuccessUrl("/user/ALL/actual")
                .permitAll();
    }

    @Bean
    public static PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}

