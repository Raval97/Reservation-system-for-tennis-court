package tennisCourt.MySQL.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name="Effective_Statistics")
public class EffectiveStatistics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    int shots_on_target;
    int accurate_passes;
    int interceptions;
    int defences;
    @OneToOne
    @MapsId
    private PlayersMatch playersMatch;

    public EffectiveStatistics() {
    }

}
