package tennisCourt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import tennisCourt.model.*;
import tennisCourt.repo.UserTournamentRepository;
import tennisCourt.security.WebSecurityConfig;
import tennisCourt.service.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.Time;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Controller
public class ControllerAdminApi {

    private UserService userService;
    private ClientService clientService;
    private PriceListService priceListService;
    private PaymentService paymentService;
    private MembershipApplicationService membershipApplicationService;
    private ClubAssociationService clubAssociationService;
    private TournamentService tournamentService;
    private UserTournamentService userTournamentService;
    private UserTournamentRepository userTournamentRepository;
    private UserTournamentApplicationService userTournamentApplicationService;
    private ReservationService reservationService;
    private UserReservationService userReservationService;
    private ReservationServicesService reservationServicesService;
    private ServicesService servicesService;
    private CourtService courtService;

    @Autowired
    public ControllerAdminApi(UserService userService, ClientService clientService, PriceListService priceListService,
                              MembershipApplicationService membershipApplicationService, PaymentService paymentService,
                              ClubAssociationService clubAssociationService, TournamentService tournamentService,
                              UserTournamentService userTournamentService, UserTournamentApplicationService userTournamentApplicationService,
                              UserTournamentRepository userTournamentRepository, ReservationService reservationService,
                              UserReservationService userReservationService, ReservationServicesService reservationServicesService,
                              ServicesService servicesService, CourtService courtService) {
        this.userService = userService;
        this.clientService = clientService;
        this.priceListService = priceListService;
        this.paymentService = paymentService;
        this.membershipApplicationService = membershipApplicationService;
        this.clubAssociationService = clubAssociationService;
        this.tournamentService = tournamentService;
        this.userTournamentService = userTournamentService;
        this.userTournamentApplicationService = userTournamentApplicationService;
        this.userTournamentRepository = userTournamentRepository;
        this.reservationService = reservationService;
        this.servicesService = servicesService;
        this.userReservationService = userReservationService;
        this.reservationServicesService = reservationServicesService;
        this.courtService = courtService;
    }

    public ControllerAdminApi() {
    }

    @RequestMapping("/admin/usersPermissions")
    public String viewAdminPermissionsPage(Model model) {
        List<User> usersList;
        List<Client> clientList;
        usersList = userService.listAll();
        clientList = clientService.listAll();
        model.addAttribute("usersList", usersList);
        model.addAttribute("clientList", clientList);
        return "admin/adminUsersPermissions";
    }

    @ResponseBody
    @RequestMapping(value = "/admin/edit_usersPermissions", method = RequestMethod.POST)
    public ResponseEntity<?> setUsersPermissions(@RequestBody Object usersPermissionsList) {
        List<String> permissionsList = (List<String>) usersPermissionsList;
        List<User> usersList;
        usersList = userService.listAll();
        final int[] iter = {0};
        usersList.forEach(user -> {
            user.setRole(permissionsList.get(iter[0]));
            userService.save(user);
            iter[0]++;
        });
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping("/admin/tournamentsAndEvents")
    public String viewAdminTournamentsPage(Model model) {
        List<Tournament> tournaments = tournamentService.listAll();
        List<List<Client>> participantTournaments = new ArrayList<>();
        List<List<Client>> participantTournamentApplications = new ArrayList<>();
        List<List<String>> participantApplicationsStatus = new ArrayList<>();
        tournaments.forEach(tournament -> {
            if (userTournamentService.countElementsByTournamentId(tournament.getId()) == 0)
                participantTournaments.add(new ArrayList<>());
            else
                participantTournaments.add(clientService.listAllByInTournamentByThemID(tournament.getId()));
            if (userTournamentApplicationService.countElementsByTournamentId(tournament.getId()) == 0) {
                participantTournamentApplications.add(new ArrayList<>());
                participantApplicationsStatus.add(new ArrayList<>());
            } else {
                participantTournamentApplications.add(clientService.listAllByInTournamentApplicationByThemID(tournament.getId()));
                participantApplicationsStatus.add(userTournamentApplicationService.listAllStatusByTournamentId(tournament.getId()));
            }
        });
        Tournament tournament = new Tournament();
        model.addAttribute("participantTournaments", participantTournaments);
        model.addAttribute("participantTournamentApplications", participantTournamentApplications);
        model.addAttribute("participantApplicationsStatus", participantApplicationsStatus);
        model.addAttribute("tournaments", tournaments);
        model.addAttribute("tournament", tournament);
        return "admin/adminTournamentsAndEvents";
    }

    @RequestMapping("/admin/acceptEventApplication/{tournamentId}/{userId}")
    public String acceptEventApplication(@PathVariable(name = "tournamentId") Long tournamentId,
                                         @PathVariable(name = "userId") Long userId) {
        UserTournamentApplication application = userTournamentApplicationService.getByTournamentAndUserId(tournamentId, userId);
        application.setStatus("To Pay");
        User user = userService.get(userId);
        Tournament tournament = tournamentService.getById(tournamentId).get();
        Payment paymentForEvent = new Payment("Tournament Fee: " + tournament.getTitle(), tournament.getDateOfStarted(), tournament.getEntryFee(), "To Pay", user);
        paymentService.save(paymentForEvent);
        userTournamentApplicationService.save(application);
        return "redirect:/admin/tournamentsAndEvents";
    }

    @RequestMapping("/admin/rejectEventApplication/{tournamentId}/{userId}")
    public String rejectEventApplication(@PathVariable(name = "tournamentId") Long tournamentId,
                                         @PathVariable(name = "userId") Long userId) {
        UserTournamentApplication application = userTournamentApplicationService.getByTournamentAndUserId(tournamentId, userId);
        application.setStatus("Rejected");
        userTournamentApplicationService.save(application);
        return "redirect:/admin/tournamentsAndEvents";
    }

    @PostMapping("/admin/createEvent")
    public String addEvent(@ModelAttribute Tournament tournament) {
        List<Court> courts = courtService.listAll();
        tournament.setCountOFRegisteredParticipant(0);
        tournamentService.save(tournament);
        User user = userService.findUserByUsername(User.getUserName());
        UserReservation userReservation = new UserReservation(user);
        Reservation reservation = new Reservation(LocalDate.now(), "Reserved", userReservation);
        reservationService.save(reservation);
        long days = Period.between(tournament.getDateOfStarted(), tournament.getDateOfEnded()).getDays() + 1;
        for (long i = 0; i < days; i++) {
            for (Court court : courts) {
                ReservationServices reservationServices = new ReservationServices(reservation);
                Services services = new Services(tournament.getDateOfStarted().plusDays(i), 24f, LocalTime.parse("00:00:00"),
                        reservationServices, court);
                servicesService.save(services);
            }
        }
        return "redirect:/admin/tournamentsAndEvents";
    }

    @RequestMapping("/admin/removeEvent/{id}")
    public String removeEvent(@PathVariable(name = "id") Long id) {
        System.out.println("Here");
        Tournament tournament = tournamentService.getById(id).get();
        long days = Period.between(tournament.getDateOfStarted(), tournament.getDateOfEnded()).getDays() + 1;
        for (long i = 0; i < days; i++) {
            reservationServicesService.deleteAllByDate(tournament.getDateOfStarted().plusDays(i));
            servicesService.deleteAllByDate(tournament.getDateOfStarted().plusDays(i));
            userReservationService.deleteAllByDate(tournament.getDateOfStarted().plusDays(i));
            reservationService.deleteAllByDate(tournament.getDateOfStarted().plusDays(i));
        }
        tournamentService.delete(id);
        return "redirect:/admin/tournamentsAndEvents";
    }

    @RequestMapping("/admin/clubAssociations")
    public String viewAdminClubAssociationPage(Model model) {
        List<MembershipApplication> membershipApplications = membershipApplicationService.listAllActive();
        List<Client> clientApplications = clientService.getAllWhoHasActiveApplication();
        List<ClubAssociation> clubAssociations = clubAssociationService.listAll();
        List<Client> clientClubMans = clientService.getAllWhoInClub();
        model.addAttribute("membershipApplications", membershipApplications);
        model.addAttribute("clientApplications", clientApplications);
        model.addAttribute("clubAssociations", clubAssociations);
        model.addAttribute("clientClubMans", clientClubMans);
        return "admin/adminClubAssociations";
    }


    @RequestMapping("/admin/acceptApplication/{id}")
    public String acceptApplication(@PathVariable(name = "id") Long id) {
        MembershipApplication membershipApplication = membershipApplicationService.getByID(id);
        membershipApplication.setDecision("accept");
        membershipApplicationService.save(membershipApplication);
        clubAssociationService.save(new ClubAssociation(LocalDate.now(), false, membershipApplication.getUser()));
        Client client = clientService.getByUserId(membershipApplication.getUser().getId());
        client.setIsClubMen(true);
        clientService.save(client);
        return "redirect:/admin/clubAssociations";
    }

    @RequestMapping("/admin/noAcceptApplication/{id}")
    public String noAcceptApplication(@PathVariable(name = "id") Long id) {
        Optional<MembershipApplication> membershipApplication = membershipApplicationService.get(id);
        membershipApplication.get().setDecision("rejected");
        membershipApplicationService.save(membershipApplication.get());
        return "redirect:/admin/clubAssociations";
    }

    @RequestMapping("/admin/priceList")
    public String viewAdminPriceListPage(Model model) {
        List<PriceList> priceList;
        priceList = priceListService.listAll();
        model.addAttribute("priceList", priceList);
        return "admin/adminPriceList";
    }

    @ResponseBody
    @RequestMapping(value = "/admin/edit_priceList", method = RequestMethod.POST)
    public ResponseEntity<?> updatePriceOfPriceList(@RequestBody Object priceList) {
        List<String> priceToChange = (List<String>) priceList;
        List<PriceList> actualPriceList;
        actualPriceList = priceListService.listAll();
        final int[] iter = {0};
        actualPriceList.forEach(ele -> {
            Float price = Float.parseFloat(priceToChange.get(iter[0]));
            ele.setPrice(price);
            priceListService.save(ele);
            iter[0]++;
        });
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}