package tennisCourt.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;

@Data
@Entity
@Table(name="user_reservation")
public class UserReservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn
    private User user;
    @OneToOne(cascade =  CascadeType.REMOVE)
    private Reservation reservation;

    public UserReservation() {
    }
}
