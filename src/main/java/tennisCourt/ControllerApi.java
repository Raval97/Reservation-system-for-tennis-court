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

import javax.servlet.http.HttpServletRequest;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
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


    //############## ADMIN ##########################################
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

    @RequestMapping("/admin/tournamentsAndEvents")
    public String viewAdminTournamentsPage(Model model) {
        return "admin/adminTournamentsAndEvents";
    }

    @RequestMapping("/admin/teamAssociations")
    public String viewAdminEventsPage(Model model) {
        return "admin/adminTeamAssociations";
    }

    @RequestMapping("/admin/priceList")
    public String viewAdminPriceListPage(Model model) {
        List<PriceList> priceList;
        priceList = priceListService.listAll();
        model.addAttribute("priceList", priceList);
        return "admin/adminPriceList";
    }

    @ResponseBody
    @RequestMapping(value = "/admin/edit_usersPermissions", method = RequestMethod.POST)
    public ResponseEntity<?> setUsersPermissions(@RequestBody Object usersPermissionsList) {
        List<String> permissionsList = (List<String>) usersPermissionsList;
        List<User> usersList;
        usersList = userService.listAll();
        final int[] iter = {0};
        usersList.forEach(user ->{
            user.setRole(permissionsList.get(iter[0]));
            userService.save(user);
            iter[0]++;
        });
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
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

    //############## USER ##########################################
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

    //############## CLIENT ##########################################

    @RequestMapping("/OurTennis/reservation")
    public String viewReservationPage(Model model, HttpServletRequest request,
                                      @RequestParam(value = "date", required = false) String date) {
        Boolean correctParameter = true;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate now = LocalDate.now();
        if (date == null)
            date = dtf.format(now);
        else {
            try {
                LocalDate localDate = LocalDate.parse(date);
                if (localDate.compareTo(now) >= 0 && localDate.compareTo(now.plusDays(27)) <= 0)
                    date = dtf.format(localDate);
                else
                    correctParameter = false;
            } catch (Exception e) {
                correctParameter = false;
            }
        }
        if (correctParameter) {
            boolean isAdmin = false;
            if (request.isUserInRole("ADMIN"))
                isAdmin = true;
            model.addAttribute("isAdmin", isAdmin);
            User user = userService.findUserByUsername(User.getUserName());
            List<Time> reservedTimeList = servicesService.getReservedTimeByDate(date);
            List<Float> reservedNumberOfHoursList = servicesService.getReservedNumberOfHoursByDate(date);
            List<Long> reservedCourtIdList = servicesService.getReservedCourtIdByDate(date);
            List<Time> startedTimeList = servicesService.getStartedTimeByDate(date, user.getId());
            List<Float> startedNumberOfHoursList = servicesService.getStartedNumberOfHoursByDate(date, user.getId());
            List<Long> startedCourtIdList = servicesService.getStartedCourtIdByDate(date, user.getId());
            List<Services> startedReservationServices = servicesService.getInStartedReservationByUserId(user.getId());
            Reservation startedReservation = reservationService.getStartedReservationByUserId(user.getId());
            model.addAttribute("reservedTimeList", reservedTimeList);
            model.addAttribute("reservedNumberOfHoursList", reservedNumberOfHoursList);
            model.addAttribute("reservedCourtIdList", reservedCourtIdList);
            model.addAttribute("startedTimeList", startedTimeList);
            model.addAttribute("startedNumberOfHoursList", startedNumberOfHoursList);
            model.addAttribute("startedCourtIdList", startedCourtIdList);
            model.addAttribute("startedReservationServices", startedReservationServices);
            model.addAttribute("startedReservation", startedReservation);
            model.addAttribute("date", date);
            return "loggedUser/reservationPage";
        } else
            return "redirect:/OurTennis/reservation";
    }

    ////////////////// Update Started Reservation ///////////////////////////////
    @ResponseBody
    @RequestMapping(value = "/saveRemovedDay", method = RequestMethod.POST)
    public ResponseEntity<?> saveRemovedDays(@RequestBody Object removedNodeArray) {
        if (!((List) removedNodeArray).isEmpty()) {
            User user = userService.findUserByUsername(User.getUserName());
            List<Services> startedReservation = servicesService.getInStartedReservationByUserId(user.getId());
            Map<String, Float> startedNoteMap = parseReservedReservationToMapFormat(startedReservation);
            Map<String, Float> removedNoteMap = new HashMap<>();
            ((List) removedNodeArray).forEach((ele) -> removedNoteMap.put((String) ele, 0.5F));
            List<String> startedNodeIdList = new ArrayList<>(startedNoteMap.keySet());
            List<String> removedNodeIdList = new ArrayList<>(removedNoteMap.keySet());
            for (String startedNodeId : startedNodeIdList) {
                LocalTime startedTime = LocalTime.of(Integer.parseInt(startedNodeId.substring(16, 18)),
                        Integer.parseInt(startedNodeId.substring(19, 21)));
                String nodeIdToRemove = startedNodeId;
                Float hourOfReservation = startedNoteMap.get(nodeIdToRemove);
                startedNoteMap.remove(nodeIdToRemove);
                for (int i = 0; i < (2 * hourOfReservation); i++) {
                    startedNoteMap.put(startedNodeId, 0.5F);
                    startedTime = startedTime.plusMinutes(30L);
                    String hour = (startedTime.getHour() > 9) ? String.valueOf(startedTime.getHour()) : "0" + startedTime.getHour();
                    String minutes = (startedTime.getMinute() > 9) ? String.valueOf(startedTime.getMinute()) : "0" + startedTime.getMinute();
                    startedNodeId = startedNodeId.substring(0, 16) + hour + "m" + minutes;
                }
            }
            for (String removedNodeId : removedNodeIdList) {
                startedNoteMap.remove(removedNodeId);
            }
            Reservation reservation = reservationService.getStartedReservationByUserId(user.getId());
            TreeMap<String, Float> nodeTreeMap = connectToSingleReservation(startedNoteMap);
            saveUpdatedServicesToStartedReservation(nodeTreeMap, reservation);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ResponseBody
    @RequestMapping(value = "/saveSelectedDay", method = RequestMethod.POST)
    public ResponseEntity<?> saveSelectedDays(@RequestBody Object selectNodeArray) {
        if (!((List) selectNodeArray).isEmpty()) {
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
        }
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
                    if (checkIsPriceOfHourChange(date, time1, finishTime)) {
                        nodeMap.replace(nodeIdList.get(iter), nodeMap.get(nodeIdList.get(iter)) + nodeMap.get(nodeIdList.get(iter + 1)));
                        nodeMap.remove(nodeIdList.get(iter + 1));
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
        if (!isWeekend)
            return startedTimeOfDay.equals(finishTimeOfDay);
        else if ((startedTimeOfDay.equals("Night") && finishTime.equals("Morning")) ||
                (startedTimeOfDay.equals("Afternoon") && finishTime.equals("Night")))
            return false;
        return true;
    }

    private String timeOfDay(LocalTime time) {
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
        final float[] finalPrice = {0};
        nodeMap.forEach((k, v) -> {
            date[0] = LocalDate.of(Integer.parseInt(k.substring(7, 11)),
                    Integer.parseInt(k.substring(4, 6)), Integer.parseInt(k.substring(1, 3)));
            time[0] = LocalTime.of(Integer.parseInt(k.substring(16, 18)), Integer.parseInt(k.substring(19, 21)));
            court[0] = courtService.get(Character.getNumericValue(k.charAt(13)));
            price[0] = getPriceByDateAndTime(date[0], time[0], v);
            reservationServices[0] = new ReservationServices(reservation);
            servicesService.save(new Services(date[0], (v), time[0], price[0], false,
                    false, false, (v * price[0]), reservationServices[0], court[0]));
            finalPrice[0] += v * price[0];
        });
        reservation.setFinalPrice(finalPrice[0]);
        reservationService.save(reservation);
    }

    private float getPriceByDateAndTime(LocalDate date, LocalTime time, float countOfHours) {
        List<PriceList> priceList;
        priceList = priceListService.listAll();
        boolean isAfternoon = time.compareTo(LocalTime.of(14, 0)) >= 0 &&
                time.compareTo(LocalTime.of(23, 0)) < 0;
        boolean isMorning = time.compareTo(LocalTime.of(6, 0)) >= 0 &&
                time.compareTo(LocalTime.of(14, 0)) < 0;
        boolean isWeekend = date.getDayOfWeek().getValue() > 5;
        if (!isAfternoon && !isMorning)
            return priceList.get(3).getPrice();
        else {
            if (isWeekend) {
                if (countOfHours >= 5)
                    return priceList.get(2).getPrice();
                return priceList.get(5).getPrice();
            } else {
                if (countOfHours >= 5)
                    return priceList.get(4).getPrice();
                if (isAfternoon)
                    return priceList.get(1).getPrice();
                else
                    return priceList.get(0).getPrice();
            }
        }
    }

    ////////////////// Make Reservation ///////////////////////////////

    @RequestMapping("/OurTennis/makeReservation")
    public String viewMakeReservationPage(Model model) {
        User user = userService.findUserByUsername(User.getUserName());
        List<Services> servicesList = servicesService.getInStartedReservationByUserId(user.getId());
        Reservation reservation = reservationService.getStartedReservationByUserId(user.getId());
        List<LocalDate> dates = new ArrayList<>();
        servicesList.forEach((x) -> dates.add(x.getDate()));
        LocalDate minDate = Collections.min(dates);
        reservation.setFinalPaymentDate(minDate);
        reservationService.save(reservation);
        model.addAttribute("servicesList", servicesList);
        model.addAttribute("reservation", reservation);
        return "loggedUser/clientMakeReservationPage";
    }

    @RequestMapping(value = "/OurTennis/confirmReservation", method = RequestMethod.POST)
    public String confirmReservation(@ModelAttribute Reservation reservation) {
        User user = userService.findUserByUsername(User.getUserName());
        Reservation startedReservation = reservationService.getStartedReservationByUserId(user.getId());
        reservationService.update(startedReservation.getId(), "Reserved", "To Pay",
                reservation.getTypeOfPaying(), LocalDate.now());

        return "redirect:/OurTennis/clientReservation";
    }

    @ResponseBody
    @RequestMapping(value = "/updateIfBalls/{id}", method = RequestMethod.POST)
    public ResponseEntity<?> updateIfBallsOfService(@PathVariable(name = "id") Long id,
                                                    @RequestBody Object ifBalls) {
        Boolean decision = ifBalls.equals("1");
        servicesService.updateIfBalls(id, decision);
        updatePrices(id, 2, decision);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ResponseBody
    @RequestMapping(value = "/updateIfRocket/{id}", method = RequestMethod.POST)
    public ResponseEntity<?> updateIfRocketOfService(@PathVariable(name = "id") Long id,
                                                     @RequestBody Object isRocket) {
        Boolean decision = isRocket.equals("1");
        servicesService.updateIfRocket(id, decision);
        updatePrices(id, 5, decision);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ResponseBody
    @RequestMapping(value = "/updateIfShoes/{id}", method = RequestMethod.POST)
    public ResponseEntity<?> updateIfShoesOfService(@PathVariable(name = "id") Long id,
                                                    @RequestBody Object ifShoes) {
        Boolean decision = ifShoes.equals("1");
        servicesService.updateIfShoes(id, decision);
        updatePrices(id, 2, decision);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    void updatePrices(Long servicesId, float price, boolean selected) {
        User user = userService.findUserByUsername(User.getUserName());
        Reservation startedReservation = reservationService.getStartedReservationByUserId(user.getId());
        Services services = servicesService.get(servicesId);
        if (selected) {
            servicesService.updatePrice(servicesId, services.getPrice() + price);
            reservationService.updatePrice(startedReservation.getId(), startedReservation.getFinalPrice() + price);
        } else {
            servicesService.updatePrice(servicesId, services.getPrice() - price);
            reservationService.updatePrice(startedReservation.getId(), startedReservation.getFinalPrice() - price);
        }
    }

    //############## CLIENT ACCOUNT ##########################################

    @RequestMapping("/OurTennis/account/{type}")
    public String viewClientAccount(@PathVariable(name = "type") Double type, Model model, HttpServletRequest request) {
        boolean isAdmin = false;
        if (request.isUserInRole("ADMIN"))
            isAdmin = true;
        model.addAttribute("isAdmin", isAdmin);
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
        return "loggedUser/clientAccountPage";
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
    public String viewClientReservationPage(Model model, HttpServletRequest request) {
        boolean isAdmin = false;
        if (request.isUserInRole("ADMIN"))
            isAdmin = true;
        model.addAttribute("isAdmin", isAdmin);
        User user = userService.findUserByUsername(User.getUserName());
        List<Reservation> reservationList = reservationService.listAllByUserId(user.getId());
        model.addAttribute("reservationList", reservationList);
        return "loggedUser/clientReservationPage";
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
        return "loggedUser/clientReservationDetailsPage";
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
        User user = userService.findUserByUsername(User.getUserName());
        List<Reservation> reservationList = reservationService.listAllByUserId(user.getId());
        model.addAttribute("reservationList", reservationList);
        return "loggedUser/clientPaymentPage";
    }

    @RequestMapping("/bankSimulator/{id}")
    public String viewBankSimulatorPage(Model model, HttpServletRequest request,
                                        @PathVariable(name = "id") Long id) {
        boolean isAdmin = false;
        if (request.isUserInRole("ADMIN"))
            isAdmin = true;
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("reservationID", id);
        return "loggedUser/bankSimulatorPage";
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
        return "defaultUser/registrationPage";
    }

    @PostMapping("/add-user")
    public String addUser(@ModelAttribute User user, @ModelAttribute Client client) {
        User newUser = new User(user.getUsername(), user.getPassword(), "ROLE_USER", client);
        userService.save(newUser);
        return "redirect:/ourTennis";
    }

}