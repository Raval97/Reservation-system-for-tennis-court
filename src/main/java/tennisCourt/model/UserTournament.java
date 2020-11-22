package tennisCourt.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
    @JoinColumn(name = "tournament")
    private Tournament tournament;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "users")
    private Users users;

    public UserTournament() {
    }

    public UserTournament(Tournament tournament, Users user) {
        this.tournament = tournament;
        this.users = user;
    }
}
