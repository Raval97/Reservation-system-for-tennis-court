package tennisCourt.MySQL.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name="Player_Match_Performance")
public class PlayerMatchPerformance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn
    private Matches matches;
    @ManyToOne
    @JoinColumn
    private Player player;
    @JsonBackReference
    @OneToOne(cascade =  CascadeType.REMOVE)
    private Performance performance;

    public PlayerMatchPerformance() {
    }

    public PlayerMatchPerformance(Player player, Matches matches) {
        this.player = player;
        this.matches = matches;
    }
}
