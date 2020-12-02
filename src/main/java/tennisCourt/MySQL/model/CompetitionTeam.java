package tennisCourt.MySQL.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name="Competition_Team")
public class CompetitionTeam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn
    private Competition competition;
    @ManyToOne
    @JoinColumn
    private Team team;

    public CompetitionTeam() {
    }

    public CompetitionTeam(Competition competition, Team team) {
        this.competition = competition;
        this.team = team;
    }
}
