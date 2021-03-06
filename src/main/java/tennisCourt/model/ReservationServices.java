package tennisCourt.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import javax.persistence.*;

@Data
@Entity
@Table(name="reservation_services")
public class ReservationServices {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn
    private Reservation reservation;
    @OneToOne(cascade =  CascadeType.REMOVE)
    private Services services;

    public ReservationServices() {
    }

    public ReservationServices(Reservation reservation) {
        this.reservation = reservation;
    }
}
