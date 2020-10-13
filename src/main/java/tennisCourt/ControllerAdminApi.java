package tennisCourt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import tennisCourt.model.*;
import tennisCourt.security.WebSecurityConfig;
import tennisCourt.service.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.Time;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Controller
public class ControllerAdminApi {

    private UserService userService;
    private ClientService clientService;
    private PriceListService priceListService;
    private MembershipApplicationService membershipApplicationService;
    private ClubAssociationService clubAssociationService;
    private TournamentService tournamentService;
    private UserTournamentService userTournamentService;
    private UserTournamentApplicationService userTournamentApplicationService;

    @Autowired
    public ControllerAdminApi(UserService userService, ClientService clientService, PriceListService priceListService,
                              MembershipApplicationService membershipApplicationService,
                              ClubAssociationService clubAssociationService, TournamentService tournamentService,
                              UserTournamentService userTournamentService, UserTournamentApplicationService userTournamentApplicationService) {
        this.userService = userService;
        this.clientService = clientService;
        this.priceListService = priceListService;
        this.membershipApplicationService = membershipApplicationService;
        this.clubAssociationService = clubAssociationService;
        this.tournamentService = tournamentService;
        this.userTournamentService = userTournamentService;
        this.userTournamentApplicationService = userTournamentApplicationService;
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
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
        System.out.println("xxxxxxxxxxxxxxxxxxxxxx");
        System.out.println(userTournamentService.getById(1L));
        System.out.println("xxxxxxxxxxxxxxxxxxxxxx");
        List<List<Client>> participantTournaments = new ArrayList<>();
        List<List<Client>> participantTournamentApplications = new ArrayList<>();
        List<List<String>> participantApplicationsStatus = new ArrayList<>();
        tournaments.forEach(tournament -> {
            if (userTournamentService.countElementsByTournamentId(tournament.getId())==0)
                participantTournaments.add(new ArrayList<>());
            else
                participantTournaments.add(clientService.listAllByInTournamentByThemID(tournament.getId()));
            if (userTournamentApplicationService.countElementsByTournamentId(tournament.getId())==0) {
                participantTournamentApplications.add(new ArrayList<>());
                participantApplicationsStatus.add(new ArrayList<>());
            } else {
                participantTournamentApplications.add(clientService.listAllByInTournamentApplicationByThemID(tournament.getId()));
                participantApplicationsStatus.add(userTournamentApplicationService.listAllStatusByTournamentId(tournament.getId()));
            }
        });
        System.out.println(participantTournaments);
        System.out.println(participantTournamentApplications);
        System.out.println(participantApplicationsStatus);
        model.addAttribute("participantTournaments", participantTournaments);
        model.addAttribute("participantTournamentApplications", participantTournamentApplications);
        model.addAttribute("participantApplicationsStatus", participantApplicationsStatus);
        model.addAttribute("tournaments", tournaments);
        return "admin/adminTournamentsAndEvents";
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