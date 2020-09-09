package tennisCourt.model;

import lombok.Data;

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
    private Users users;
    @OneToOne(cascade =  CascadeType.REMOVE)
    private Reservation reservation;

    public UserReservation() {
    }

    public UserReservation(Users users) {
        this.users = users;
    }
}
