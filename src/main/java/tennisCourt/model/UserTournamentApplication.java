package tennisCourt.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name="user_tournament_application")
public class UserTournamentApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String status;
    private boolean ifPaidEntryFee;
    @ManyToOne
    @JoinColumn
    private Tournament tournament;
    @ManyToOne
    @JoinColumn
    private Users users;

    public UserTournamentApplication() {
    }

    public UserTournamentApplication(String status, boolean ifPaidEntryFee, Tournament tournament, Users user) {
        this.status = status;
        this.ifPaidEntryFee = ifPaidEntryFee;
        this.tournament = tournament;
        this.users = user;
    }

}
