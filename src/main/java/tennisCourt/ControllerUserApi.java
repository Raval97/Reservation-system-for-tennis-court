package tennisCourt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import tennisCourt.model.*;
import tennisCourt.service.*;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Controller
public class ControllerUserApi {

    private UserService userService;
    private PriceListService priceListService;
    private TournamentService tournamentService;
    private UserTournamentService userTournamentService;
    private UserTournamentApplicationService userTournamentApplicationService;

    @Autowired
    public ControllerUserApi(UserService userService, PriceListService priceListService, TournamentService tournamentService,
                             UserTournamentService userTournamentService,  UserTournamentApplicationService userTournamentApplicationService) {
        this.userService = userService;
        this.priceListService = priceListService;
        this.tournamentService = tournamentService;
        this.userTournamentService = userTournamentService;
        this.userTournamentApplicationService = userTournamentApplicationService;
    }

    public ControllerUserApi() {
    }

    @RequestMapping("/ourTennis")
    public String viewStartPage(Model model, HttpServletRequest request) {
        boolean logged = false;
        boolean isAdmin = false;
        if (request.isUserInRole("USER"))
            logged = true;
        if (request.isUserInRole("ADMIN")) {
            logged = true;
            isAdmin = true;
        }
        model.addAttribute("logged", logged);
        model.addAttribute("isAdmin", isAdmin);
        return "defaultUser/startPage";
    }

    @RequestMapping("/ourTennis/events")
    public String viewTournamentsAndEventsPage(Model model, HttpServletRequest request) {
        boolean logged = false;
        boolean isAdmin = false;
        if (request.isUserInRole("USER"))
            logged = true;
        if (request.isUserInRole("ADMIN")) {
            logged = true;
            isAdmin = true;
        }
        List<String> userInTournamentsStatus = new ArrayList<>();
        if(logged){
            User user = userService.findUserByUsername(User.getUserName());
            userInTournamentsStatus = userTournamentApplicationService.getAllStatusTournamentToUser(user.getId());
        }
        System.out.println(userInTournamentsStatus);
        List<Tournament> tournaments = tournamentService.listAll();
        model.addAttribute("logged", logged);
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("tournaments", tournaments);
        model.addAttribute("userStatusList", userInTournamentsStatus);
        return "defaultUser/tournamentsEventsPage";
    }

    @RequestMapping("/ourTennis/priceList")
    public String viewPriceListPage(Model model, HttpServletRequest request) {
        boolean logged = false;
        boolean isAdmin = false;
        if (request.isUserInRole("USER"))
            logged = true;
        if (request.isUserInRole("ADMIN")) {
            logged = true;
            isAdmin = true;
        }
        model.addAttribute("logged", logged);
        model.addAttribute("isAdmin", isAdmin);
        List<PriceList> priceList;
        priceList = priceListService.listAll();
        model.addAttribute("priceList", priceList);
        return "defaultUser/priceListPage";
    }

    @RequestMapping("/ourTennis/contact")
    public String viewContactPage(Model model, HttpServletRequest request) {
        boolean logged = false;
        boolean isAdmin = false;
        if (request.isUserInRole("USER"))
            logged = true;
        if (request.isUserInRole("ADMIN")) {
            logged = true;
            isAdmin = true;
        }
        model.addAttribute("logged", logged);
        model.addAttribute("isAdmin", isAdmin);
        return "defaultUser/contactPage";
    }

    @RequestMapping("/ourTennis/gallery")
    public String viewGalleryPage(Model model, HttpServletRequest request) {
        boolean logged = false;
        boolean isAdmin = false;
        if (request.isUserInRole("USER"))
            logged = true;
        if (request.isUserInRole("ADMIN")) {
            logged = true;
            isAdmin = true;
        }
        model.addAttribute("logged", logged);
        model.addAttribute("isAdmin", isAdmin);
        return "defaultUser/galleryPage";
    }

    //################  LOGOWANIE & REJESTRACJA  ##################################

    @GetMapping("/registration")
    public String test(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        model.addAttribute("client", new Client());
        return "defaultUser/registrationPage";
    }

    @PostMapping("/add-user")
    public String addUser(@ModelAttribute User user, @ModelAttribute Client client) {
        client.setIsClubMen(false);
        User newUser = new User(user.getUsername(), user.getPassword(), "ROLE_USER", client);
        userService.save(newUser);
        return "redirect:/ourTennis";
    }

}