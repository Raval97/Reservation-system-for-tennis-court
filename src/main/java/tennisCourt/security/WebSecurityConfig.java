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
import tennisCourt.model.Court;
import tennisCourt.model.PriceList;
import tennisCourt.model.Users;
import tennisCourt.repo.ClientRepository;
import tennisCourt.repo.CourtRepository;
import tennisCourt.repo.PriceListRepository;
import tennisCourt.repo.UserRepository;
import tennisCourt.service.UserService;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private UserService userDetailsService;
    private UserRepository userRepository;
    private ClientRepository clientRepository;
    private PriceListRepository priceListRepository;
    private CourtRepository courtRepository;

    @Autowired
    public WebSecurityConfig(UserService userDetailsService, UserRepository userRepository, ClientRepository clientRepository,
                             PriceListRepository priceListRepository, CourtRepository courtRepository) {
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
        this.clientRepository = clientRepository;
        this.priceListRepository = priceListRepository;
        this.courtRepository = courtRepository;
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


    @EventListener(ApplicationReadyEvent.class)
    public void get() {
        PriceList p1 = new PriceList(1L,"From Monday to Friday[1h]", "6.00-14.00", 40F);
        PriceList p2 = new PriceList(2L,"From Monday to Friday[1h]", "14-23.00", 50F);
        PriceList p3 = new PriceList(3L,"From Saturday to Sunday[1h]", "06.00-23.00", 45F);
        PriceList p4 = new PriceList(4L,"Night Gaming [1h]", "23.00-01.00", 30F);
        PriceList p5 = new PriceList(5L,"From Monday to Friday[1h]", "5 or more hours Tennis Pass", 30F);
        PriceList p6 = new PriceList(6L,"From Saturday to Sunday[1h]", "5 or more hours Tennis Pass", 40F);
        PriceList p7 = new PriceList(7L,"Rocket Rental", "The Duration Of The Game", 5F);
        PriceList p8 = new PriceList(8L,"Balls Rental", "The Duration Of The Game", 2F);
        PriceList p9 = new PriceList(9L,"Shoes Rental", "The Duration Of The Game", 2F);
        priceListRepository.save(p1); priceListRepository.save(p2); priceListRepository.save(p3);
        priceListRepository.save(p4); priceListRepository.save(p5); priceListRepository.save(p6);
        priceListRepository.save(p7); priceListRepository.save(p8); priceListRepository.save(p9);
        Court court1 = new Court(1L, "court_1", "ziemny", true);
        Court court2 = new Court(2L, "court_2", "ziemny", true);
        Court court3 = new Court(3L, "court_3", "ziemny", true);
        Court court4 = new Court(4L, "court_4", "ziemny", true);
        courtRepository.save(court1); courtRepository.save(court2);
        courtRepository.save(court3); courtRepository.save(court4);
        Client client = new Client("Adam", "Malysz", "adam.malysz@gmail.com", 123456789);
        Users appUserClient1 = new Users("user", passwordEncoder().encode("user"), "ROLE_USER", client);
        userRepository.save(appUserClient1);
    }

}

