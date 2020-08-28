package tennisCourt;

import jdk.jfr.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import tennisCourt.model.*;
import tennisCourt.security.WebSecurityConfig;
import tennisCourt.service.*;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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

    //############## USER ##########################################
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
        return "priceListPage";
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

    //############## CLIENT ##########################################
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
        return "priceListPage";
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
    public String viewReservationPage(Model model,
                                      @RequestParam(value = "date", required = false) String date) {
        if(date == null){
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDateTime now = LocalDateTime.now();
            date = dtf.format(now);
        }
        User user = userService.findUserByUsername(User.getUserName());
        List<Time> reservedTimeList = servicesService.getReservedTimeByDate(date);
        List<Float> reservedNumberOfHoursList = servicesService.getReservedNumberOfHoursByDate(date);
        List<Long> reservedCourtIdList = servicesService.getReservedCourtIdByDate(date);
        List<Time> startedTimeList = servicesService.getStartedTimeByDate(date, user.getId());
        List<Float> startedNumberOfHoursList = servicesService.getStartedNumberOfHoursByDate(date, user.getId());
        List<Long> startedCourtIdList = servicesService.getStartedCourtIdByDate(date, user.getId());
        model.addAttribute("reservedTimeList", reservedTimeList);
        model.addAttribute("reservedNumberOfHoursList", reservedNumberOfHoursList);
        model.addAttribute("reservedCourtIdList", reservedCourtIdList);
        model.addAttribute("startedTimeList", startedTimeList);
        model.addAttribute("startedNumberOfHoursList", startedNumberOfHoursList);
        model.addAttribute("startedCourtIdList", startedCourtIdList);
        model.addAttribute("date", date);
        return "reservationPage";
    }

    @RequestMapping("/reservation")
    public String example(Model model,
                          @RequestParam(value = "date", required = false) String date) {
        if(date == null){
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDateTime now = LocalDateTime.now();
            date = dtf.format(now);
        }
        User user = userService.findUserByUsername(User.getUserName());
        System.out.println("date= "+date);
        List<Time> reservedTimeList = servicesService.getReservedTimeByDate(date);
        List<Float> reservedNumberOfHoursList = servicesService.getReservedNumberOfHoursByDate(date);
        List<Long> reservedCourtIdList = servicesService.getReservedCourtIdByDate(date);
        List<Time> startedTimeList = servicesService.getStartedTimeByDate(date, user.getId());
        List<Float> startedNumberOfHoursList = servicesService.getStartedNumberOfHoursByDate(date, user.getId());
        List<Long> startedCourtIdList = servicesService.getStartedCourtIdByDate(date, user.getId());
        System.out.println(reservedTimeList);
        System.out.println(reservedNumberOfHoursList);
        System.out.println(reservedCourtIdList);
        System.out.println(startedTimeList);
        System.out.println(startedNumberOfHoursList);
        System.out.println(startedCourtIdList);
        model.addAttribute("reservedTimeList", reservedTimeList);
        model.addAttribute("reservedNumberOfHoursList", reservedNumberOfHoursList);
        model.addAttribute("reservedCourtIdList", reservedCourtIdList);
        model.addAttribute("startedTimeList", startedTimeList);
        model.addAttribute("startedNumberOfHoursList", startedNumberOfHoursList);
        model.addAttribute("startedCourtIdList", startedCourtIdList);
        model.addAttribute("date", date);
        return "reservationPage";
    }

    @ResponseBody
    @RequestMapping(value = "/saveSelectedDay", method = RequestMethod.POST)
    public ResponseEntity<Category> saveSelectedDays(@RequestBody Object selectNodeArray) {
        System.out.println("response: "+selectNodeArray.toString());
        List<String> selectNodeList = (List)selectNodeArray;
        User user = userService.findUserByUsername(User.getUserName());
        if (!reservationService.checkIfUserHasStartedReservation(user.getId())){
            UserReservation userReservation = new UserReservation(user);
            reservationService.save(new Reservation(null, 0, "Started",
                null, null, null, userReservation));
        }
        Reservation reservation = reservationService.getStartedReservationByUserId(user.getId());
        ReservationServices reservationServices = null;
        LocalDate date = null;
        LocalTime time  = null;
        Court court = null;
        for (String selectNode: selectNodeList){
            date = LocalDate.of(2020, Integer.parseInt(selectNode.substring(4,6)), Integer.parseInt(selectNode.substring(1,3)));
            time =  LocalTime.of(Integer.parseInt(selectNode.substring(8,10)),Integer.parseInt(selectNode.substring(11,13)));
            court = courtService.get(Character.getNumericValue(selectNode.charAt(15)));
//            court = courtService.get(3L);
            System.out.println(selectNode.charAt(15)+" court_id ");
            System.out.println(selectNode.charAt(15)+" court_id "+court.getId());
            reservationServices= new ReservationServices(reservation);
            servicesService.save(new Services(date, 0.5F, time, 45,
                    false, false, false, 22.5F, reservationServices, court));
        }
        selectNodeList.forEach(x ->  System.out.println(x));
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
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
        return "clientAccountPage";
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
            return "redirect:/OurTennis/account/1.1";
        }
        else
            return "redirect:/OurTennis/account/1";
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
                return "redirect:/OurTennis/account/2.1";
            }
            else
                return "redirect:/OurTennis/account/2.2";
        } else
            return "redirect:/OurTennis/account/2.3";
    }

    @RequestMapping("/OurTennis/client/deleteAccount/{id}")
    public String deleteBetFromCoupon(@PathVariable(name = "id") int id) {
        clientService.deleteByUserId(id);
        User user = userService.get(id);
        user.setPassword("cannotLogin");
        userService.save(user);
        return "redirect:/ourTennis";
    }

    @RequestMapping("/OurTennis/clientReservation")
    public String viewClientReservationPage(Model model) {
        User user = userService.findUserByUsername(User.getUserName());
        List<Reservation> reservationList = reservationService.listAllByUserId(user.getId());
        model.addAttribute("reservationList", reservationList);
        return "clientReservationPage";
    }

    @RequestMapping("/OurTennis/clientReservation/{id}")
    public String viewClientReservationDetailsPage(Model model,
                                                   @PathVariable(name = "id") Long id) {
        Reservation reservation = reservationService.get(id);
        List<Services> servicesList = servicesService.listAllByReservationId(id);
        model.addAttribute("reservation", reservation);
        model.addAttribute("servicesList", servicesList);
        return "clientReservationDetailsPage";
    }

    @RequestMapping("/OurTennis/cancelReservation/{id}")
    public String cancelReservation(Model model,
                                    @PathVariable(name = "id") Long id) {
        reservationServicesService.deleteAllByReservationId(id);
        userReservationService.delete(userReservationService.getByReservationId(id).getId());
        return "redirect:/OurTennis/clientReservation";
    }

    @RequestMapping("/OurTennis/payment")
    public String viewClientPaymentPage(Model model) {
        User user = userService.findUserByUsername(User.getUserName());
        List<Reservation> reservationList = reservationService.listAllByUserId(user.getId());
        model.addAttribute("reservationList", reservationList);
        return "clientPaymentPage";
    }

    @RequestMapping("/bankSimulator/{id}")
    public String viewBankSimulatorPage(Model model,
                                        @PathVariable(name = "id") Long id){
        model.addAttribute("reservationID", id);
        return "bankSimulatorPage";
    }

    @RequestMapping("/payForReservation/{id}")
    public String setStatusForPayment(@PathVariable(name = "id") Long id){
        Reservation reservation = reservationService.get(id);
        reservation.setStatusPaying("Paid");
        reservationService.save(reservation);
        return "redirect:/OurTennis/payment";
    }

    //################  LOGOWANIE & REJESTRACJA  ##################################

    @GetMapping("/registration")
    public String test(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        model.addAttribute("client", new Client());
        return "registrationPage";
    }

    @PostMapping("/add-user")
    public String addUser(@ModelAttribute User user, @ModelAttribute Client client) {
        User newUser = new User(user.getUsername(), user.getPassword(), "ROLE_USER", client);
        userService.save(newUser);
        return "redirect:/ourTennis";
    }

}