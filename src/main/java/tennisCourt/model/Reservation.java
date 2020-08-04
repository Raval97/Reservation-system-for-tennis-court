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
    private Long id;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfReservation;
    private float finalPrice;
    private String statusOfReservation;
    private String typeOfPaying;
    private String statusPaying;
    @OneToOne(mappedBy = "reservation", cascade = CascadeType.ALL)
    private UserReservation userReservation;
    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL)
    private Set<ReservationServices> reservationServices;

    public Reservation(Long id, LocalDate dateOfReservation, float finalPrice,
                       String statusOfReservation, String typeOfPaying, String statusPaying) {
        this.id = id;
        this.dateOfReservation = dateOfReservation;
        this.finalPrice = finalPrice;
        this.statusOfReservation = statusOfReservation;
        this.typeOfPaying = typeOfPaying;
        this.statusPaying = statusPaying;
    }
}
