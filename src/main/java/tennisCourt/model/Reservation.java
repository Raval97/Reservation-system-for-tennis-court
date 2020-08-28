package tennisCourt.model;

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
    private LocalDate dateOfReservation;
    private float finalPrice;
    private String statusOfReservation;
    private String typeOfPaying;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate finalPaymentDate;
    private String statusPaying;
    @OneToOne(mappedBy = "reservation", cascade = CascadeType.ALL)
    private UserReservation userReservation;
    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL)
    private Set<ReservationServices> reservationServices;

    public Reservation() {
    }

    public Reservation(LocalDate dateOfReservation, float finalPrice, String statusOfReservation,
                       String typeOfPaying, LocalDate finalPaymentDate, String statusPaying,
                       UserReservation userReservation) {
        this.dateOfReservation = dateOfReservation;
        this.finalPrice = finalPrice;
        this.statusOfReservation = statusOfReservation;
        this.typeOfPaying = typeOfPaying;
        this.finalPaymentDate = finalPaymentDate;
        this.statusPaying = statusPaying;
        this.userReservation = userReservation;
        this.userReservation.setReservation(this);
    }
}
