package tennisCourt.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
@Entity
@Table(name="reservation")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private boolean byAdmin;
    private LocalDate dateOfReservation;
    private float price;
    private float finalPrice;
    private boolean discount;
    private String statusOfReservation;
    private String typeOfPaying;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate finalPaymentDate;
    private String statusPaying;
    @JsonBackReference
    @OneToOne(mappedBy = "reservation", cascade = CascadeType.ALL)
    private UserReservation userReservation;
    @JsonBackReference
    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL)
    private Set<ReservationServices> reservationServices;

    public Reservation() {
    }

    public Reservation(LocalDate dateOfReservation, float price, boolean discount, float finalPrice,
                       String statusOfReservation, String typeOfPaying, LocalDate finalPaymentDate,
                       String statusPaying, UserReservation userReservation) {
        this.dateOfReservation = dateOfReservation;
        this.finalPrice = finalPrice;
        this.price = price;
        this.discount = discount;
        this.statusOfReservation = statusOfReservation;
        this.typeOfPaying = typeOfPaying;
        this.finalPaymentDate = finalPaymentDate;
        this.statusPaying = statusPaying;
        this.userReservation = userReservation;
        this.userReservation.setReservation(this);
        this.byAdmin = false;
    }

    public Reservation(LocalDate dateOfReservation, String statusOfReservation, UserReservation userReservation) {
        this.byAdmin = true;
        this.dateOfReservation = dateOfReservation;
        this.statusOfReservation = statusOfReservation;
        this.userReservation = userReservation;
        this.userReservation.setReservation(this);
    }
}
