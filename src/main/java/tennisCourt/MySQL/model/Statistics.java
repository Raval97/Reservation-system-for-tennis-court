package tennisCourt.MySQL.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name="Statistics")
public class Statistics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    int shots;
    int passes;
    int interceptions_attempts;
    int defences_attempts;
    @OneToOne
    @MapsId
    private PlayersMatch playersMatch;

    public Statistics() {
    }

}
