package tennisCourt.model;

import lombok.Data;
import javax.persistence.*;

@Data
@Entity
@Table(name="reservation_services")
public class ReservationServices {

    @Id
    private Long id;
    @ManyToOne
    @JoinColumn
    private Reservation reservation;
    @OneToOne
    private Services services;
}
