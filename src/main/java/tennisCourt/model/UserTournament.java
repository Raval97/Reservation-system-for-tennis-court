package tennisCourt.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import javax.persistence.*;

@Data
@Entity
@Table(name="user_tournament")
public class UserTournament {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "tournament_id")
    private Tournament tournament;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public UserTournament() {
    }

    public UserTournament(Tournament tournament, User user) {
        this.tournament = tournament;
        this.user = user;
    }

}
