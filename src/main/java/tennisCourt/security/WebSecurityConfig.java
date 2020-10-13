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
import tennisCourt.model.*;
import tennisCourt.repo.*;
import tennisCourt.service.UserService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private UserService userDetailsService;
    private UserRepository userRepository;
    private PriceListRepository priceListRepository;
    private CourtRepository courtRepository;
    private MembershipApplicationRepository membershipApplicationRepository;
    private ClubAssociationRepository clubAssociationRepository;
    private ReservationRepository reservationRepository;
    private ServicesRepository servicesRepository;
    private PaymentRepository paymentRepository;
    private TournamentRepository tournamentRepository;
    private UserTournamentRepository userTournamentRepository;
    private UserTournamentApplicationRepository userTournamentApplicationRepository;

    @Autowired
    public WebSecurityConfig(UserService userDetailsService, UserRepository userRepository, PriceListRepository priceListRepository,
                             CourtRepository courtRepository, MembershipApplicationRepository membershipApplicationRepository,
                             ClubAssociationRepository clubAssociationRepository, ReservationRepository reservationRepository,
                             ServicesRepository servicesRepository, PaymentRepository paymentRepository,
                             TournamentRepository tournamentRepository, UserTournamentRepository userTournamentRepository,
                             UserTournamentApplicationRepository userTournamentApplicationRepository) {
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
        this.priceListRepository = priceListRepository;
        this.courtRepository = courtRepository;
        this.membershipApplicationRepository = membershipApplicationRepository;
        this.clubAssociationRepository = clubAssociationRepository;
        this.reservationRepository = reservationRepository;
        this.servicesRepository = servicesRepository;
        this.paymentRepository = paymentRepository;
        this.tournamentRepository = tournamentRepository;
        this.userTournamentRepository = userTournamentRepository;
        this.userTournamentApplicationRepository = userTournamentApplicationRepository;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/OurTennis/**").hasAnyRole("ADMIN", "USER")
                .antMatchers("/admin/**").hasRole("ADMIN")
                .and()
                .formLogin().permitAll().defaultSuccessUrl("/ourTennis", true)
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
//        PriceList p1 = new PriceList(1L,"From Monday to Friday[1h]", "6.00-14.00", 40F);
//        PriceList p2 = new PriceList(2L,"From Monday to Friday[1h]", "14-23.00", 50F);
//        PriceList p3 = new PriceList(3L,"From Saturday to Sunday[1h]", "06.00-23.00", 45F);
//        PriceList p4 = new PriceList(4L,"Night Gaming [1h]", "23.00-01.00", 30F);
//        PriceList p5 = new PriceList(5L,"From Monday to Friday[1h]", "5 or more hours Tennis Pass", 30F);
//        PriceList p6 = new PriceList(6L,"From Saturday to Sunday[1h]", "5 or more hours Tennis Pass", 40F);
//        PriceList p7 = new PriceList(7L,"Rocket Rental", "The Duration Of The Game", 5F);
//        PriceList p8 = new PriceList(8L,"Balls Rental", "The Duration Of The Game", 2F);
//        PriceList p9 = new PriceList(9L,"Shoes Rental", "The Duration Of The Game", 2F);
//        priceListRepository.save(p1); priceListRepository.save(p2); priceListRepository.save(p3);
//        priceListRepository.save(p4); priceListRepository.save(p5); priceListRepository.save(p6);
//        priceListRepository.save(p7); priceListRepository.save(p8); priceListRepository.save(p9);
//        Court court1 = new Court(1L, "court_1", "ziemny", true);
//        Court court2 = new Court(2L, "court_2", "ziemny", true);
//        Court court3 = new Court(3L, "court_3", "ziemny", true);
//        Court court4 = new Court(4L, "court_4", "ziemny", true);
//        courtRepository.save(court1); courtRepository.save(court2);
//        courtRepository.save(court3); courtRepository.save(court4);
//        Client client1 = new Client("Adam", "Malysz", "adam.malysz@gmail.com", 123456789);
//        Client client2 = new Client("Robert", "Kubica", "robert.kubica@gmail.com", 111222333);
//        Client client3 = new Client("Rafal", "Gegotek", "rafal.gg@gmail.com", 987654321);
//        client3.setIsClubMen(true);
//        User appUserClient1 = new User("user", passwordEncoder().encode("user"), "ROLE_USER", client1);
//        User appUserClient2 = new User("user1", passwordEncoder().encode("user1"), "ROLE_USER", client2);
//        User appUserClient3 = new User("admin", passwordEncoder().encode("admin"), "ROLE_ADMIN", client3);
//        userRepository.save(appUserClient1);
//        userRepository.save(appUserClient2);
//        userRepository.save(appUserClient3);
//        MembershipApplication membershipApplicationClient1 = new MembershipApplication(appUserClient1, LocalDate.now(), "waiting_for_decisions");
//        MembershipApplication membershipApplicationClient2 = new MembershipApplication(appUserClient2, LocalDate.now(), "waiting_for_decisions");
//        membershipApplicationRepository.save(membershipApplicationClient1);
//        membershipApplicationRepository.save(membershipApplicationClient2);
//        ClubAssociation clubAssociationClient3 = new ClubAssociation(LocalDate.now(), true, appUserClient3);
//        clubAssociationClient3.setDateOfEndActive(LocalDate.now().plusDays(30));
//        clubAssociationRepository.save(clubAssociationClient3);
//        Payment paymentForMembership = new Payment("Membership fee", LocalDate.now(), 30F, "Paid", appUserClient3);
//        Payment paymentForEvent1 = new Payment("Tournamed Fee: Cracow Open",
//                LocalDate.now(), 20F, "To Pay", appUserClient1);
//        Payment paymentForEvent2 = new Payment("Tournamed Fee: Cracow Open",
//                LocalDate.now(), 20F, "Paid", appUserClient3);
//        paymentRepository.save(paymentForMembership);
//        paymentRepository.save(paymentForEvent1);
//        paymentRepository.save(paymentForEvent2);
//        Tournament tournament1 = new Tournament("Cracow Open", 4, 1,
//                LocalDate.now().plusDays(3), LocalDate.now().plusDays(3), 20F);
//        Tournament tournament2 = new Tournament("Tennis Cup", 2, 0,
//                LocalDate.now().plusDays(15), LocalDate.now().plusDays(16), 15F);
//        tournamentRepository.save(tournament1);
//        tournamentRepository.save(tournament2);
//        UserTournamentApplication userTournamentApplication1 =
//                new UserTournamentApplication("To Pay", true, tournament1, appUserClient1);
//        UserTournamentApplication userTournamentApplication2 =
//                new UserTournamentApplication("delivered", false, tournament1, appUserClient2);
//        UserTournamentApplication userTournamentApplication3 =
//                new UserTournamentApplication("accepted", true, tournament1, appUserClient3);
//        userTournamentApplicationRepository.save(userTournamentApplication1);
//        userTournamentApplicationRepository.save(userTournamentApplication2);
//        userTournamentApplicationRepository.save(userTournamentApplication3);
//        UserTournament userTournament = new UserTournament(tournament1, appUserClient3);
//        userTournamentRepository.save(userTournament);
//        UserReservation userReservation1 = new UserReservation(appUserClient3);
//        UserReservation userReservation2 = new UserReservation(appUserClient3);
//        Reservation reservation1 = new Reservation(LocalDate.now(), 171F, true, 145.35F,
//                "Reserved", "online", LocalDate.now(), "Paid", userReservation1);
//        Reservation reservation2 = new Reservation(LocalDate.now().minusDays(1), "Reserved", userReservation2);
//        reservationRepository.save(reservation1);
//        reservationRepository.save(reservation2);
//        ReservationServices reservationServices1 = new ReservationServices(reservation1);
//        ReservationServices reservationServices2 = new ReservationServices(reservation1);
//        ReservationServices reservationServices3 = new ReservationServices(reservation1);
//        ReservationServices reservationServices4 = new ReservationServices(reservation1);
//        ReservationServices reservationServices5 = new ReservationServices(reservation1);
//        ReservationServices reservationServices6 = new ReservationServices(reservation1);
//        ReservationServices reservationServices7 = new ReservationServices(reservation1);
//        Services services1 = new Services(LocalDate.now().plusDays(1), 2.5F, LocalTime.parse("11:30:00", DateTimeFormatter.ISO_TIME),
//                40F, false, true, false, 102, reservationServices1, court1);
//        Services services2 = new Services(LocalDate.now().plusDays(1), 0.5F, LocalTime.parse("09:30:00", DateTimeFormatter.ISO_TIME),
//                40F, true, false, true, 27, reservationServices2, court3);
//        Services services3 = new Services(LocalDate.now().plusDays(3), 1.0F, LocalTime.parse("11:00:00", DateTimeFormatter.ISO_TIME),
//                40F, false, true, false, 42, reservationServices3, court4);
//        Services services4 = new Services(LocalDate.now().plusDays(3), 10F, LocalTime.parse("08:00:00"), reservationServices4, court1);
//        Services services5 = new Services(LocalDate.now().plusDays(3), 10F, LocalTime.parse("08:00:00"), reservationServices5, court1);
//        Services services6 = new Services(LocalDate.now().plusDays(3), 10F, LocalTime.parse("08:00:00"), reservationServices6, court1);
//        Services services7 = new Services(LocalDate.now().plusDays(3), 10F, LocalTime.parse("08:00:00"), reservationServices7, court1);
//        servicesRepository.save(services1);
//        servicesRepository.save(services2);
//        servicesRepository.save(services3);
//        servicesRepository.save(services4);
//        servicesRepository.save(services5);
//        servicesRepository.save(services6);
//        servicesRepository.save(services7);
//    }

}

