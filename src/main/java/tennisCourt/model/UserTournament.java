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
    @JoinColumn(name = "tournament_id")
    private Tournament tournament;
    @ManyToOne
    @JoinColumn(name = "users_id")
    private Users users;

    public UserTournament() {
    }

    public UserTournament(Tournament tournament, Users user) {
        this.tournament = tournament;
        this.users = user;
    }
}
