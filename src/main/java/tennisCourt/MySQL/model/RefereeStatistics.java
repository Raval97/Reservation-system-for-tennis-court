package tennisCourt.MySQL.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name="Referee_Statistics")
public class RefereeStatistics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    int fouls;
    int yellow_cards;
    int red_cards;
    int goals;
    int assists;
    @OneToOne
    @MapsId
    private PlayersMatch playersMatch;

    public RefereeStatistics() {
    }

}
