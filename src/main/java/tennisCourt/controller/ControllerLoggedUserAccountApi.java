package tennisCourt.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import tennisCourt.model.*;
import tennisCourt.security.WebSecurityConfig;
import tennisCourt.service.*;

import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

@Controller
public class ControllerLoggedUserAccountApi {

    private UserService userService;
    private ClientService clientService;
    private ReservationService reservationService;
    private ServicesService servicesService;
    private UserReservationService userReservationService;
    private ReservationServicesService reservationServicesService;
    private MembershipApplicationService membershipApplicationService;
    private ClubAssociationService clubAssociationService;
    private PaymentService paymentService;

    @Autowired
    public ControllerLoggedUserAccountApi(UserService userService, ClientService clientService,
                                          ReservationService reservationService, UserReservationService userReservationService,
                                          ServicesService servicesService, ReservationServicesService reservationServicesService,
                                          MembershipApplicationService membershipApplicationService,
                                          ClubAssociationService clubAssociationService, PaymentService paymentService) {
        this.userService = userService;
        this.clientService = clientService;
        this.reservationService = reservationService;
        this.servicesService = servicesService;
        this.userReservationService = userReservationService;
        this.reservationServicesService = reservationServicesService;
        this.membershipApplicationService = membershipApplicationService;
        this.clubAssociationService = clubAssociationService;
        this.paymentService = paymentService;
    }

    public ControllerLoggedUserAccountApi() {
    }

    @RequestMapping("/OurTennis/account/{type}")
    public String viewClientAccount(@PathVariable(name = "type") Double type, Model model, HttpServletRequest request) {
        boolean isAdmin = false;
        if (request.isUserInRole("ADMIN"))
            isAdmin = true;
        model.addAttribute("isAdmin", isAdmin);
        Users user = userService.findUserByUsername(Users.getUserName());
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
        return "loggedUser/clientAccountPersonalData";
    }

    @RequestMapping(value = "/OurTennis/client/edit_data/{id}", method = RequestMethod.POST)
    public String editClientData(@RequestBody String object, @PathVariable(name = "id") Long id,
                                 @ModelAttribute("client") Client client,
                                 @ModelAttribute("user") Users user) throws JsonProcessingException {

        Users userToChange = userService.get(id);
        Client clientToChange = clientService.get(id);
        if (object.startsWith("{")) {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(object);
            clientToChange.setName(mapper.convertValue(jsonNode.get("name"), String.class));
            clientToChange.setSurname(mapper.convertValue(jsonNode.get("surname"), String.class));
            clientToChange.setEmailAddress(mapper.convertValue(jsonNode.get("emailAddress"), String.class));
            clientToChange.setPhoneNumber(mapper.convertValue(jsonNode.get("phoneNumber"), int.class));
            userToChange.setUsername(mapper.convertValue(jsonNode.get("username"), String.class));
            userToChange.setClient(clientToChange);
            userService.save(userToChange);
        } else {
            if ((!userToChange.getUsername().equals(user.getUsername())) || (!clientToChange.equals(client))) {
                userToChange.setUsername(user.getUsername());
                client.setIsClubMen(clientToChange.getIsClubMen());
                userToChange.setClient(client);
                userService.save(userToChange);
                return "redirect:/OurTennis/account/1.1";
            } else
                return "redirect:/OurTennis/account/1";
        }
        return "redirect:/OurTennis/account/1";
    }

    @RequestMapping(value = "/OurTennis/client/changePassword/{id}", method = RequestMethod.POST)
    public String changeClientPassword(@PathVariable(name = "id") Long id,
                                       @ModelAttribute("user") Users user,
                                       @RequestParam("newPassword") String newPassword,
                                       @RequestParam("repeatNewPassword") String repeatNewPassword,
                                       @RequestParam("oldPassword") String oldPassword) {
        Users userToChange = userService.get(id);
        if (WebSecurityConfig.passwordEncoder().matches(oldPassword, userToChange.getPassword())) {
            System.out.println(newPassword + "  " + repeatNewPassword);
            if (repeatNewPassword.equals(newPassword)) {
                userToChange.setPassword(newPassword);
                userService.save(userToChange);
                return "redirect:/OurTennis/account/2.1";
            } else
                return "redirect:/OurTennis/account/2.2";
        } else
            return "redirect:/OurTennis/account/2.3";
    }

    @RequestMapping("/OurTennis/client/deleteAccount/{id}")
    public String deleteBetFromCoupon(@PathVariable(name = "id") int id) {
        clientService.deleteByUserId(id);
        Users user = userService.get(id);
        user.setPassword("cannotLogin");
        userService.save(user);
        return "redirect:/ourTennis";
    }

    @RequestMapping("/OurTennis/clientReservation")
    public String viewClientReservationPage(Model model, HttpServletRequest request) {
        boolean isAdmin = false;
        if (request.isUserInRole("ADMIN"))
            isAdmin = true;
        model.addAttribute("isAdmin", isAdmin);
        Users user = userService.findUserByUsername(Users.getUserName());
        List<Reservation> reservationList = reservationService.listAllByUserId(user.getId());
        model.addAttribute("reservationList", reservationList);
        return "loggedUser/clientAccountReservation";
    }

    @RequestMapping("/OurTennis/clientReservation/{id}")
    public String viewClientReservationDetailsPage(Model model, HttpServletRequest request,
                                                   @PathVariable(name = "id") Long id) {
        boolean isAdmin = false;
        if (request.isUserInRole("ADMIN"))
            isAdmin = true;
        model.addAttribute("isAdmin", isAdmin);
        Reservation reservation = reservationService.get(id);
        List<Services> servicesList = servicesService.listAllByReservationId(id);
        model.addAttribute("reservation", reservation);
        model.addAttribute("servicesList", servicesList);
        return "loggedUser/clientAccountReservationDetails";
    }

    @RequestMapping("/OurTennis/cancelReservation/{id}")
    public String cancelReservation(Model model,
                                    @PathVariable(name = "id") Long id) {
        reservationServicesService.deleteAllByReservationId(id);
        userReservationService.delete(userReservationService.getByReservationId(id).getId());
        return "redirect:/OurTennis/clientReservation";
    }

    @RequestMapping("/OurTennis/payment")
    public String viewClientPaymentPage(Model model, HttpServletRequest request) {
        boolean isAdmin = false;
        if (request.isUserInRole("ADMIN"))
            isAdmin = true;
        model.addAttribute("isAdmin", isAdmin);
        Users user = userService.findUserByUsername(Users.getUserName());
        List<Reservation> reservationList = reservationService.listAllByUserId(user.getId());
        List<Payment> paymentList = paymentService.listAllByUserId(user.getId());
        model.addAttribute("reservationList", reservationList);
        model.addAttribute("paymentList", paymentList);
        return "loggedUser/clientAccountPayment";
    }

    @RequestMapping("/bankSimulatorForReservation/{id}")
    public String viewBankSimulatorForReservationPage(Model model, @PathVariable(name = "id") Long id) {
        model.addAttribute("ID", id);
        model.addAttribute("type", "forReservation");
        return "loggedUser/bankSimulatorPage";
    }

    @RequestMapping("/bankSimulatorForMembershipFee")
    public String viewBankSimulatorForMembershipFeePage(Model model) {
        Users user = userService.findUserByUsername(Users.getUserName());
        ClubAssociation clubAssociation = clubAssociationService.getByUserId(user.getId());
        model.addAttribute("ID", clubAssociation.getId());
        model.addAttribute("type", "forMembershipFee");
        return "loggedUser/bankSimulatorPage";
    }

    @RequestMapping("/payForReservation/{id}")
    public String setStatusForPayment(@PathVariable(name = "id") Long id) {
        Reservation reservation = reservationService.get(id);
        reservation.setStatusPaying("Paid");
        reservationService.save(reservation);
        return "redirect:/OurTennis/payment";
    }

    @RequestMapping("/OurTennis/clubAssociation")
    public String viewClientClubAssociationtPage(Model model, HttpServletRequest request) {
        boolean isAdmin = false;
        if (request.isUserInRole("ADMIN"))
            isAdmin = true;
        Users user = userService.findUserByUsername(Users.getUserName());
        boolean isClubMen = clientService.get(user.getId()).getIsClubMen();
        boolean isActiveClubMen = false;
        long  daysOfActiveMembership = 0;
        if(isClubMen) {
            isActiveClubMen = clubAssociationService.getByUserId(user.getId()).getIfActive();
            if(isActiveClubMen)
                daysOfActiveMembership = Duration.between(LocalDate.now().atStartOfDay(),
                        clubAssociationService.getByUserId(user.getId()).getDateOfEndActive().atStartOfDay()).toDays();
        }
        boolean hasActiveApplication = false;
        boolean hasRejectedApplication = false;
        String decision = membershipApplicationService.getLatestMembershipApplicationDecision(user.getId());
        if (decision.equals("waiting_for_decisions"))
            hasActiveApplication =true;
        if (decision.equals("rejected"))
            hasRejectedApplication =true;
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("user", user);
        model.addAttribute("isClubMen", isClubMen);
        model.addAttribute("isActiveClubMen", isActiveClubMen);
        model.addAttribute("daysOfActiveMembership", daysOfActiveMembership);
        model.addAttribute("hasActiveApplication", hasActiveApplication);
        model.addAttribute("hasRejectedApplication", hasRejectedApplication);
        return "loggedUser/clientAccountClubAssociation";
    }

    @RequestMapping("/OurTennis/payMembershipFee/{id}")
    public String payForMembership(@PathVariable(name = "id") Long id) {
        Users user = userService.findUserByUsername(Users.getUserName());
        ClubAssociation clubAssociation = clubAssociationService.getById(id);
        if(clubAssociation.getIfActive())
            clubAssociation.setDateOfEndActive(clubAssociation.getDateOfEndActive().plusDays(30));
        else {
            clubAssociation.setIfActive(true);
            clubAssociation.setDateOfEndActive(LocalDate.now().plusDays(30));
        }
        clubAssociationService.save(clubAssociation);
        Payment payment = new Payment("Membership fee", 30F, LocalDate.now(),  "Paid", user);
        paymentService.save(payment);
        return "redirect:/OurTennis/clubAssociation";
    }

    @RequestMapping("/OurTennis/applyForMembership")
    public String applyForMembership(Model model) {
        Users user = userService.findUserByUsername(Users.getUserName());
        membershipApplicationService.save(new MembershipApplication(user,  LocalDate.now(), "waiting_for_decisions"));
        return "redirect:/OurTennis/clubAssociation";
    }

}