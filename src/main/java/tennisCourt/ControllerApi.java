package tennisCourt;

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
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

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
        if (date == null) {
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
        List<Services> startedReservationServices = servicesService.getInStartedReservationByUserId(user.getId());
        model.addAttribute("reservedTimeList", reservedTimeList);
        model.addAttribute("reservedNumberOfHoursList", reservedNumberOfHoursList);
        model.addAttribute("reservedCourtIdList", reservedCourtIdList);
        model.addAttribute("startedTimeList", startedTimeList);
        model.addAttribute("startedNumberOfHoursList", startedNumberOfHoursList);
        model.addAttribute("startedCourtIdList", startedCourtIdList);
        model.addAttribute("startedReservationServices", startedReservationServices);
        model.addAttribute("date", date);
        return "reservationPage";
    }

    ////////////////// Update Started Reservation ///////////////////////////////

    @ResponseBody
    @RequestMapping(value = "/saveRemovedDay", method = RequestMethod.POST)
    public ResponseEntity<?> saveRemovedDays(@RequestBody Object removedNodeArray) {
        User user = userService.findUserByUsername(User.getUserName());
        List<Services> startedReservation = servicesService.getInStartedReservationByUserId(user.getId());
        Map<String, Float> reservedNoteMap = parseReservedReservationToMapFormat(startedReservation);
        Map<String, Float> removedNoteMap = new HashMap<>();
        ((List) removedNodeArray).forEach((ele) -> removedNoteMap.put((String) ele, 0.5F));
        List<String> nodeIdList = new ArrayList<>(removedNoteMap.keySet());
//        for (String nodeId: nodeIdList) {
//            if()
//        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @ResponseBody
    @RequestMapping(value = "/saveSelectedDay", method = RequestMethod.POST)
    public ResponseEntity<?> saveSelectedDays(@RequestBody Object selectNodeArray) {
        User user = userService.findUserByUsername(User.getUserName());
        List<Services> startedReservation = servicesService.getInStartedReservationByUserId(user.getId());
        Map<String, Float> reservedNoteMap = parseReservedReservationToMapFormat(startedReservation);
        Map<String, Float> startedNoteMap = new HashMap<>();
        ((List) selectNodeArray).forEach((ele) -> startedNoteMap.put((String) ele, 0.5F));
        startedNoteMap.putAll(reservedNoteMap);
        TreeMap<String, Float> nodeMap = connectToSingleReservation(startedNoteMap);
        if (!reservationService.checkIfUserHasStartedReservation(user.getId())) {
            UserReservation userReservation = new UserReservation(user);
            reservationService.save(new Reservation(null, 0, "Started",
                    null, null, null, userReservation));
        }
        Reservation reservation = reservationService.getStartedReservationByUserId(user.getId());
        saveUpdatedServicesToStartedReservation(nodeMap, reservation);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private Map<String, Float> parseReservedReservationToMapFormat(List<Services> startedReservation) {
        AtomicReference<String> reservedNoteId = new AtomicReference<>("");
        Map<String, Float> reservedNoteMap = new HashMap<>();
        startedReservation.forEach((x) -> {
            int day = x.getDate().getDayOfMonth();
            String stringDay = (day > 9) ? String.valueOf(day) : "0" + day;
            int month = x.getDate().getMonthValue();
            String stringMonth = (month > 9) ? String.valueOf(month) : "0" + month;
            int hour = x.getTime().getHour();
            String stringHour = (hour > 9) ? String.valueOf(hour) : "0" + hour;
            int minutes = x.getTime().getMinute();
            String stringMinutes = (minutes > 10) ? String.valueOf(minutes) : "0" + minutes;
            reservedNoteId.set("d" + stringDay + "m" + stringMonth + "r" + x.getDate().getYear() +
                    "_c" + x.getCourt().getId() + "_h" + stringHour + "m" + stringMinutes);
            reservedNoteMap.put(reservedNoteId.get(), x.getNumberOfHours());
        });
        return reservedNoteMap;
    }

    private TreeMap<String, Float> connectToSingleReservation(Map<String, Float> startedNoteMap) {
        TreeMap<String, Float> nodeMap = new TreeMap<String, Float>(startedNoteMap);
        List<String> nodeIdList = new ArrayList<>(nodeMap.keySet());
        LocalTime time1;
        LocalTime time2;
        LocalTime finishTime = null;
        int iter = 0;
        int size = nodeIdList.size();
        for (int i = 0; i < size - 1; i++) {
            if (nodeIdList.get(iter).startsWith(nodeIdList.get(iter + 1).substring(0, 15))) {
                time1 = LocalTime.of(Integer.parseInt(nodeIdList.get(iter).substring(16, 18)),
                        Integer.parseInt(nodeIdList.get(iter).substring(19, 21)));
                time2 = LocalTime.of(Integer.parseInt(nodeIdList.get(iter + 1).substring(16, 18)),
                        Integer.parseInt(nodeIdList.get(iter + 1).substring(19, 21)));
                finishTime = time1.plusMinutes((long) (30L * (2 * nodeMap.get(nodeIdList.get(iter)))));
                if (finishTime.equals(time2)) {
                    LocalDate date = LocalDate.of(Integer.parseInt(nodeIdList.get(iter).substring(7, 11)),
                            Integer.parseInt(nodeIdList.get(iter).substring(4, 6)), Integer.parseInt(nodeIdList.get(iter).substring(1, 3)));
                    if(checkIsPriceOfHourChange(date, time1, finishTime)) {
                        nodeMap.remove(nodeIdList.get(iter + 1));
                        nodeMap.replace(nodeIdList.get(iter), nodeMap.get(nodeIdList.get(iter)) + 0.5F);
                        nodeIdList.remove(iter + 1);
                        iter--;
                    }
                }
            }
            iter++;
        }
        return nodeMap;
    }

    private Boolean checkIsPriceOfHourChange(LocalDate date, LocalTime statedTime, LocalTime finishTime) {
        String startedTimeOfDay = timeOfDay(statedTime);
        String finishTimeOfDay = timeOfDay(finishTime);
        boolean isWeekend = date.getDayOfWeek().getValue() > 4;
        if(!isWeekend)
            return startedTimeOfDay.equals(finishTimeOfDay);
        else
            if ((startedTimeOfDay.equals("Night") && finishTime.equals("Morning")) ||
                    (startedTimeOfDay.equals("Afternoon") && finishTime.equals("Night")))
                return false;
        return true;
    }

    private String  timeOfDay(LocalTime time){
        if (time.compareTo(LocalTime.of(14, 0)) >= 0 &&
                time.compareTo(LocalTime.of(23, 0)) < 0)
            return "Afternoon";
        else if (time.compareTo(LocalTime.of(6, 0)) >= 0 &&
                time.compareTo(LocalTime.of(14, 0)) < 0)
            return "Morning";
        else
            return "Night";
    }

    private void saveUpdatedServicesToStartedReservation(TreeMap<String, Float> nodeMap, Reservation reservation) {
        reservationServicesService.deleteAllByReservationId(reservation.getId());
        final ReservationServices[] reservationServices = {null};
        final LocalDate[] date = {null};
        final LocalTime[] time = {null};
        final Court[] court = {null};
        final float[] price = {0};
        nodeMap.forEach((k, v) -> {
            date[0] = LocalDate.of(Integer.parseInt(k.substring(7, 11)),
                    Integer.parseInt(k.substring(4, 6)), Integer.parseInt(k.substring(1, 3)));
            time[0] = LocalTime.of(Integer.parseInt(k.substring(16, 18)), Integer.parseInt(k.substring(19, 21)));
            court[0] = courtService.get(Character.getNumericValue(k.charAt(13)));
            price[0] = getPriceByDateAndTime(date[0], time[0], v);
            reservationServices[0] = new ReservationServices(reservation);
            servicesService.save(new Services(date[0], (v), time[0], price[0], false,
                    false, false, (v * price[0]), reservationServices[0], court[0]));
        });
    }

    private float getPriceByDateAndTime(LocalDate date, LocalTime time, float countOfHours) {
        Boolean isAfternoon = time.compareTo(LocalTime.of(14, 0)) >= 0 &&
                time.compareTo(LocalTime.of(23, 0)) < 0;
        Boolean isMorning = time.compareTo(LocalTime.of(6, 0)) >= 0 &&
                time.compareTo(LocalTime.of(14, 0)) < 0;
        Boolean isWeekend = date.getDayOfWeek().getValue() > 4;
        if (!isAfternoon && !isMorning)
            return 30F;
        else {
            if (isWeekend) {
                if (countOfHours >= 10)
                    return 40F;
                return 45F;
            } else {
                if (countOfHours == 10)
                    return 30F;
                if (isAfternoon)
                    return 50F;
                else
                    return 40F;
            }
        }
    }

    ////////////////// Update Started Reservation ///////////////////////////////


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
        if ((!userToChange.getUsername().equals(user.getUsername())) || (!clientToChange.equals(client))) {
            userToChange.setUsername(user.getUsername());
            userToChange.setClient(client);
            userService.save(userToChange);
            return "redirect:/OurTennis/account/1.1";
        } else
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
                                        @PathVariable(name = "id") Long id) {
        model.addAttribute("reservationID", id);
        return "bankSimulatorPage";
    }

    @RequestMapping("/payForReservation/{id}")
    public String setStatusForPayment(@PathVariable(name = "id") Long id) {
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