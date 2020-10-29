package tennisCourt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import tennisCourt.model.*;
import tennisCourt.service.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Controller
public class ControllerLoggedUserApi {

    private UserService userService;
    private ClientService clientService;
    private CourtService courtService;
    private ReservationService reservationService;
    private ServicesService servicesService;
    private ReservationServicesService reservationServicesService;
    private PriceListService priceListService;
    private ClubAssociationService clubAssociationService;
    private PaymentService paymentService;
    private TournamentService tournamentService;
    private UserTournamentService userTournamentService;
    private UserTournamentApplicationService userTournamentApplicationService;

    @Autowired
    public ControllerLoggedUserApi(UserService userService, ClientService clientService, CourtService courtService,
                                   ReservationService reservationService, ServicesService servicesService,
                                   ReservationServicesService reservationServicesService, PaymentService paymentService,
                                   PriceListService priceListService, ClubAssociationService clubAssociationService,
                                   TournamentService tournamentService, UserTournamentService userTournamentService,
                                   UserTournamentApplicationService userTournamentApplicationService) {
        this.userService = userService;
        this.clientService = clientService;
        this.courtService = courtService;
        this.reservationService = reservationService;
        this.servicesService = servicesService;
        this.reservationServicesService = reservationServicesService;
        this.paymentService = paymentService;
        this.priceListService = priceListService;
        this.clubAssociationService = clubAssociationService;
        this.tournamentService = tournamentService;
        this.userTournamentService = userTournamentService;
        this.userTournamentApplicationService = userTournamentApplicationService;
    }

    public ControllerLoggedUserApi() {
    }

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
            return "loggedUser/clientReservation";
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
                Client client = clientService.getByUserId(user.getId());
                boolean discount =false;
                if (client.getIsClubMen())
                    discount = clubAssociationService.getByUserId(user.getId()).getIfActive();
                reservationService.save(new Reservation(null, 0, discount, 0,
                        "Started", null, null, null, userReservation));
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
        reservation.setPrice(finalPrice[0]);
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
        Client client = clientService.getByUserId(user.getId());
        boolean discount =false;
        if (client.getIsClubMen())
            discount = clubAssociationService.getByUserId(user.getId()).getIfActive();
        reservation.setDiscount(discount);
        if(reservation.isDiscount())
            reservation.setFinalPrice((float) (reservation.getPrice()*0.85));
        else
            reservation.setFinalPrice(reservation.getPrice());
        reservationService.save(reservation);
        model.addAttribute("servicesList", servicesList);
        model.addAttribute("reservation", reservation);
        return "loggedUser/clientMakeReservation";
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
        float price = priceListService.listAll().get(7).getPrice();
        Boolean decision = ifBalls.equals("1");
        servicesService.updateIfBalls(id, decision);
        updatePrices(id, price, decision);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ResponseBody
    @RequestMapping(value = "/updateIfRocket/{id}", method = RequestMethod.POST)
    public ResponseEntity<?> updateIfRocketOfService(@PathVariable(name = "id") Long id,
                                                     @RequestBody Object isRocket) {
        float price = priceListService.listAll().get(6).getPrice();
        Boolean decision = isRocket.equals("1");
        servicesService.updateIfRocket(id, decision);
        updatePrices(id, price, decision);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ResponseBody
    @RequestMapping(value = "/updateIfShoes/{id}", method = RequestMethod.POST)
    public ResponseEntity<?> updateIfShoesOfService(@PathVariable(name = "id") Long id,
                                                    @RequestBody Object ifShoes) {
        float price = priceListService.listAll().get(8).getPrice();
        Boolean decision = ifShoes.equals("1");
        servicesService.updateIfShoes(id, decision);
        updatePrices(id, price, decision);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    void updatePrices(Long servicesId, float price, boolean selected) {
        User user = userService.findUserByUsername(User.getUserName());
        Reservation startedReservation = reservationService.getStartedReservationByUserId(user.getId());
        Services services = servicesService.get(servicesId);
        if (selected) {
            servicesService.updatePrice(servicesId, services.getPrice() + price);
            reservationService.updatePrice(startedReservation.getId(), startedReservation.getPrice() + price);
        } else {
            servicesService.updatePrice(servicesId, services.getPrice() - price);
            reservationService.updatePrice(startedReservation.getId(), startedReservation.getPrice() - price);
        }
    }

    ////////////////// Tournaments & Events Api ///////////////////////////////
    @RequestMapping("/OurTennis/applyForParticipantEvent/{id}")
    public String applyForParticipantEvent(@PathVariable(name = "id") Long id) {
        User user = userService.findUserByUsername(User.getUserName());
        Tournament tournament = tournamentService.getById(id).get();
        UserTournamentApplication application = new UserTournamentApplication("Delivered", false, tournament, user);
        userTournamentApplicationService.save(application);
        return "redirect:/ourTennis/events";
    }

    @RequestMapping("/OurTennis/payFeeOfParticipantEvent/{id}")
    public String viewBankSimulatorForParticipantEventPage(Model model, @PathVariable(name = "id") Long id) {
        model.addAttribute("ID", id);
        model.addAttribute("type", "forEventApplicationFee");
        return "loggedUser/bankSimulatorPage";
    }

    @RequestMapping("/OurTennis/payFeeOfParticipantEventFromAccount/{id}")
    public String viewBankSimulatorForParticipantEventFromAccountPage(Model model, @PathVariable(name = "id") Long id) {
        Payment payment = paymentService.getById(id).get();
        System.out.println(payment.getTitle().substring(16));
        Tournament  tournament = tournamentService.getByTitle(payment.getTitle().substring(16));
        model.addAttribute("ID", tournament.getId());
        model.addAttribute("type", "forEventApplicationFee");
        return "loggedUser/bankSimulatorPage";
    }

    @RequestMapping("/OurTennis/payEventApplicationFee/{id}")
    public String payEventApplicationFee(@PathVariable(name = "id") Long id) {
        User user = userService.findUserByUsername(User.getUserName());
        Tournament tournament = tournamentService.getById(id).get();
        tournament.setCountOFRegisteredParticipant(tournament.getCountOFRegisteredParticipant()+1);
        UserTournamentApplication application = userTournamentApplicationService.getByTournamentAndUserId(tournament.getId(), user.getId());
        application.setStatus("Accepted");
        Payment payment = paymentService.getByTitleAndUser("Tournament Fee: "+tournament.getTitle(), user.getId());
        payment.setStatusPaying("Paid");
        payment.setDateOfPaying(LocalDate.now());
        UserTournament userTournament = new UserTournament(tournament, user);
        userTournamentApplicationService.save(application);
        paymentService.save(payment);
        tournamentService.save(tournament);
        userTournamentService.save(userTournament);
        return "redirect:/OurTennis/payment";
    }

    @RequestMapping("/OurTennis/cancelApplicationEvent/{id}")
    public String cancelApplicationEvent(@PathVariable(name = "id") Long id) {
        User user = userService.findUserByUsername(User.getUserName());
        Tournament tournament = tournamentService.getById(id).get();
        UserTournamentApplication application = userTournamentApplicationService.getByTournamentAndUserId(tournament.getId(),user.getId());
        if(application.getStatus().equals("Rejected")){
            Payment payment = paymentService.getByTitleAndUser(tournament.getTitle(), user.getId());
            paymentService.delete(payment.getId());
        }
        application.setStatus("Canceled");
        userTournamentApplicationService.save(application);
        return "redirect:/ourTennis/events";
    }

    @RequestMapping("/OurTennis/cancelParticipantEvent/{id}")
    public String cancelParticipantEvent(@PathVariable(name = "id") Long id) {
        User user = userService.findUserByUsername(User.getUserName());
        Tournament tournament = tournamentService.getById(id).get();
        tournament.setCountOFRegisteredParticipant(tournament.getCountOFRegisteredParticipant()-1);
        UserTournamentApplication application = userTournamentApplicationService.getByTournamentAndUserId(tournament.getId(),user.getId());
        application.setStatus("Canceled");
        userTournamentService.deleteByTournamentAndUserId(tournament.getId(), user.getId());
        userTournamentApplicationService.save(application);
        tournamentService.save(tournament);
        return "redirect:/ourTennis/events";
    }

}