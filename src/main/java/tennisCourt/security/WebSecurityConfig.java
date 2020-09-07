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
import tennisCourt.model.PriceList;
import tennisCourt.model.User;
import tennisCourt.repo.ClientRepository;
import tennisCourt.repo.PriceListRepository;
import tennisCourt.repo.UserRepository;
import tennisCourt.service.UserService;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private UserService userDetailsService;
    private UserRepository userRepository;
    private ClientRepository clientRepository;
    private PriceListRepository priceListRepository;

    @Autowired
    public WebSecurityConfig(UserService userDetailsService, UserRepository userRepository,
                             ClientRepository clientRepository, PriceListRepository priceListRepository) {
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
        this.clientRepository = clientRepository;
        this.priceListRepository = priceListRepository;
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
                .permitAll()
                .and()
                .csrf().disable().cors();
    }

    @Bean
    public static PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

//    @EventListener(ApplicationReadyEvent.class)
//    public void get() {
//        Client client = new Client("Adam", "Malysz", "adam.malysz@gmail.com", 123456789);
//        User appUserClient1 = new User("user", passwordEncoder().encode("user"), "ROLE_USER", client);
//        userRepository.save(appUserClient1);
//        PriceList p1 = new PriceList(1L,"morning", "6.00-14.00", 40F);
//        PriceList p2 = new PriceList(2L,"afternoon", "14.00-23.00", 50F);
//        PriceList p3 = new PriceList(3L,"night", "14.00-23.00", 30F);
//        priceListRepository.save(p1);
//        priceListRepository.save(p2);
//        priceListRepository.save(p3);
//    }

}

