package tennisCourt.model;

import lombok.Data;
import javax.persistence.*;

@Data
@Entity
@Table(name="user_tournament")
public class UserTournament {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn
    private Tournament tournament;
    @ManyToOne
    @JoinColumn
    private User user;

    public UserTournament() {
    }

    public UserTournament(Tournament tournament, User user) {
        this.tournament = tournament;
        this.user = user;
    }
}
