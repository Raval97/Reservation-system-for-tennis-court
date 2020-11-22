package tennisCourt.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import tennisCourt.model.Client;
import tennisCourt.model.PriceList;
import tennisCourt.model.Tournament;
import tennisCourt.model.Users;
import tennisCourt.service.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ControllerUserApi {

    private UserService userService;
    private PriceListService priceListService;
    private TournamentService tournamentService;
    private UserTournamentService userTournamentService;
    private UserTournamentApplicationService userTournamentApplicationService;

    @Autowired
    public ControllerUserApi(UserService userService, PriceListService priceListService, TournamentService tournamentService,
                             UserTournamentService userTournamentService, UserTournamentApplicationService userTournamentApplicationService) {
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
            Users user = userService.findUserByUsername(Users.getUserName());
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
        Users user = new Users();
        model.addAttribute("user", user);
        model.addAttribute("client", new Client());
        return "defaultUser/registrationPage";
    }

    @RequestMapping(value = "/add-user", method = RequestMethod.POST)
    public String addUser(@RequestBody String object, @ModelAttribute Users user, @ModelAttribute Client client) throws JsonProcessingException {
        Users newUser;
        if (object.startsWith("{")) {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(object);
            Client clientFromJson = new Client(
                    mapper.convertValue(jsonNode.get("name"), String.class),
                    mapper.convertValue(jsonNode.get("surname"), String.class),
                    mapper.convertValue(jsonNode.get("emailAddress"), String.class),
                    mapper.convertValue(jsonNode.get("phoneNumber"), int.class));
            clientFromJson.setIsClubMen(false);
            client = clientFromJson;
            Users userFromJson = new Users();
            userFromJson.setUsername(mapper.convertValue(jsonNode.get("username"), String.class));
            userFromJson.setPassword(mapper.convertValue(jsonNode.get("password"), String.class));
            user = userFromJson;
        }
        client.setIsClubMen(false);
        newUser = new Users(user.getUsername(), user.getPassword(), "ROLE_USER", client);
        userService.save(newUser);
        return "redirect:/ourTennis";
    }

    @RequestMapping("/OurTennis/login")
    public Model checkAuthorization(Model model) {
        model.addAttribute("authorization", true);
        return model;
    }

}