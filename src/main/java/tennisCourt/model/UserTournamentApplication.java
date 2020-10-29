package tennisCourt.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
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
    private User user;

    public UserTournamentApplication() {
    }

    public UserTournamentApplication(String status, boolean ifPaidEntryFee, Tournament tournament, User user) {
        this.status = status;
        this.ifPaidEntryFee = ifPaidEntryFee;
        this.tournament = tournament;
        this.user = user;
    }

}
