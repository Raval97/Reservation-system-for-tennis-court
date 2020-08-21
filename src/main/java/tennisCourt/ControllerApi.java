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
import tennisCourt.model.PriceList;
import tennisCourt.model.ReservationServices;
import tennisCourt.model.User;
import tennisCourt.repo.CourtRepository;
import tennisCourt.security.WebSecurityConfig;
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
    private PriceListService priceListService;

    @Autowired
    public ControllerApi(UserService userService, ClientService clientService, CourtService courtService,
                         ReservationService reservationService, UserReservationService userReservationService,
                         ServicesService servicesService, ReservationServicesService reservationServicesService,
                         PriceListService priceListService) {
        this.userService = userService;
        this.clientService = clientService;
        this.courtService = courtService;
        this.reservationService = reservationService;
        this.servicesService = servicesService;
        this.userReservationService = userReservationService;
        this.reservationServicesService = reservationServicesService;
        this.priceListService = priceListService;
    }

    public ControllerApi() {
    }

    //############## START USER ##########################################
    @RequestMapping("/ourTennis")
    public String viewStartPage(Model model) {
        model.addAttribute("logged", false);
        return "startPage";
    }

    @RequestMapping("/ourTennis/priceList")
    public String viewPriceListPage(Model model) {
        List<PriceList> priceList;
        priceList = priceListService.listAll();
        model.addAttribute("priceList", priceList);
        model.addAttribute("logged", false);
        return "priceList";
    }

    @RequestMapping("/ourTennis/contact")
    public String viewContactPage(Model model) {
        model.addAttribute("logged", false);
        return "contactPage";
    }

    @RequestMapping("/ourTennis/gallery")
    public String viewGalleryPage(Model model) {
        model.addAttribute("logged", false);
        return "galleryPage";
    }

    //############## START CLIENT ##########################################
    @RequestMapping("/OurTennis")
    public String viewClientPage(Model model) {
        model.addAttribute("logged", true);
        return "startPage";
    }

    @RequestMapping("/OurTennis/priceList")
    public String viewClientPriceListPage(Model model) {
        List<PriceList> priceList;
        priceList = priceListService.listAll();
        model.addAttribute("priceList", priceList);
        model.addAttribute("logged", true);
        return "priceList";
    }

    @RequestMapping("/OurTennis/contact")
    public String viewClientContactPage(Model model) {
        model.addAttribute("logged", true);
        return "contactPage";
    }

    @RequestMapping("/OurTennis/gallery")
    public String viewClientGalleryPage(Model model) {
        model.addAttribute("logged", true);
        return "galleryPage";
    }


    @RequestMapping("/OurTennis/reservation")
    public String viewReservationPage(Model model) {
        return "reservationPage";
    }

    //############## CLIENT ACCOUNT ##########################################

    @RequestMapping("/OurTennis/account/{type}")
    public String viewClientAccount(@PathVariable(name = "type") Double type, Model model) {
        User user = userService.findUserByUsername(User.getUserName());
        Client client = clientService.get(user.getId());
        model.addAttribute("client", client);
        model.addAttribute("user", user);
        model.addAttribute("type", type);
        String oldPassword = new String();
        String newPassword = new String();
        String repeatNewPassword = new String();
        model.addAttribute("oldPassword", oldPassword);
        model.addAttribute("newPassword", newPassword);
        model.addAttribute("repeatNewPassword", repeatNewPassword);
        String ifDeleteAccount = "NO";
        model.addAttribute("ifDeleteAccount", ifDeleteAccount);
        return "clientAccount";
    }

    @RequestMapping(value = "/OurTennis/client/edit_data/{id}", method = RequestMethod.POST)
    public String editClientData(@PathVariable(name = "id") Long id,
                                 @ModelAttribute("client") Client client,
                                 @ModelAttribute("user") User user) {
        User userToChange = userService.get(id);
        Client clientToChange = clientService.get(user.getId());
        if((!userToChange.getUsername().equals(user.getUsername())) || (!clientToChange.equals(client))) {
            userToChange.setUsername(user.getUsername());
            userToChange.setClient(client);
            userService.save(userToChange);
            return "redirect:/account/1.1";
        }
        else
            return "redirect:/account/1";
    }

    @RequestMapping(value = "/OurTennis/client/changePassword/{id}", method = RequestMethod.POST)
    public String changeClientPassword(@PathVariable(name = "id") Long id,
                                       @ModelAttribute("user") User user,
                                       @RequestParam String newPassword,
                                       @RequestParam String repeatNewPassword,
                                       @RequestParam String oldPassword) {
        User userToChange = userService.get(id);
        if (WebSecurityConfig.passwordEncoder().matches(oldPassword, userToChange.getPassword())) {
            System.out.println(newPassword+"  "+repeatNewPassword);
            if (repeatNewPassword.equals(newPassword)) {
                userToChange.setPassword(newPassword);
                userService.save(userToChange);
                return "redirect:/account/2.1";
            }
            else
                return "redirect:/account/2.2";
        } else
            return "redirect:/account/2.3";
    }

    @RequestMapping("/OurTennis/client/deleteAccount/{id}")
    public String deleteBetFromCoupon(@PathVariable(name = "id") int id) {
        userService.delete(id);
        return "redirect:/start";
    }

    //################  LOGOWANIE & REJESTRACJA  ##################################

    @GetMapping("/registration")
    public String test(Model model) {
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