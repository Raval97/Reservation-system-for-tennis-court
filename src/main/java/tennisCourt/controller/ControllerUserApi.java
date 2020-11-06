package tennisCourt.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jdk.nashorn.internal.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import tennisCourt.model.*;
import tennisCourt.service.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URL;
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
        if (logged) {
            User user = userService.findUserByUsername(User.getUserName());
            userInTournamentsStatus = userTournamentApplicationService.getAllStatusTournamentToUser(user.getId());
        }
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

    @RequestMapping(value = "/add-user", method = RequestMethod.POST)
    public String addUser(@RequestBody String object, @ModelAttribute User user, @ModelAttribute Client client) throws JsonProcessingException {
        User newUser;
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
            User userFromJson = new User();
            userFromJson.setUsername(mapper.convertValue(jsonNode.get("username"), String.class));
            userFromJson.setPassword(mapper.convertValue(jsonNode.get("password"), String.class));
            user = userFromJson;
        }
        client.setIsClubMen(false);
        newUser = new User(user.getUsername(), user.getPassword(), "ROLE_USER", client);
        userService.save(newUser);
        return "redirect:/ourTennis";
    }

}