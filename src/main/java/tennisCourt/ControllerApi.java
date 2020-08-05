package tennisCourt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import tennisCourt.model.Client;
import tennisCourt.model.ReservationServices;
import tennisCourt.model.User;
import tennisCourt.repo.CourtRepository;
import tennisCourt.service.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class ControllerApi {

    private UserService userService;
    private ClientService clientService;
    private CourtService courtService;
    private ReservationService reservationService;
    private ServicesService servicesService;
    private UserReservationService userReservationService;
    private ReservationServicesService reservationServicesService;

    @Autowired
    public ControllerApi(UserService userService, ClientService clientService, CourtService courtService,
                         ReservationService reservationService, UserReservationService userReservationService,
                         ServicesService servicesService, ReservationServicesService reservationServicesService) {
        this.userService = userService;
        this.clientService = clientService;
        this.courtService = courtService;
        this.reservationService = reservationService;
        this.servicesService = servicesService;
        this.userReservationService = userReservationService;
        this.reservationServicesService = reservationServicesService;
    }

    public ControllerApi() {
    }

    //############## START USER ##########################################
    @RequestMapping("/start")
    public String viewStartPage(Model model) {
        return "startPage";
    }


    //############## START CLIENT ##########################################
    @RequestMapping("/client")
    public String viewClientPage(Model model) {
        return "clientPage";
    }

    @RequestMapping("/account")
    public String viewClientAccount(Model model) {
        User user = userService.findUserByUsername(User.getUserName());
        Client client = clientService.get(user.getId());
        model.addAttribute("client", client);
        model.addAttribute("user", user);
        return "clientAccount";
    }

    @RequestMapping(value = "/client/edit_data/{id}", method = RequestMethod.POST)
    public String editClientData(@PathVariable(name = "id") Long id,
                                 @ModelAttribute ("client") Client client,
                                 @ModelAttribute ("user") User user){
        User userToChange = userService.get(id);
        userToChange.setUsername(user.getUsername());
        userToChange.setClient(client);
        userService.save(userToChange);
        return "redirect:/account";
    }
    //bład: edyttuje dane ale nie aktualizuje ich w programie i nie moze odczytac zaktualizowanego id bo go nie widzi


    //################  LOGOWANIE & REJESTRACJA  ##################################

    @GetMapping("/registration")
    public  String test(Model model){
        User user = new User();
        model.addAttribute("user", user);
        model.addAttribute("client", new Client());
        return "registration";
    }

    @PostMapping("/add-user")
    public String addUser(@ModelAttribute User user, @ModelAttribute Client client) {
        User newUser = new User(user.getUsername(), user.getPassword(), "ROLE_USER", client);
        userService.save(newUser);
        return "redirect:/start";
    }

}