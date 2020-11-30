package tennisCourt.MySQL.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name="player_team")
public class PlayerTeam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn
    private Team team;
    @OneToOne(cascade =  CascadeType.REMOVE)
    private PersonalData player;

    public PlayerTeam() {
    }

    public PlayerTeam(Team team) {
        this.team = team;
    }
}
