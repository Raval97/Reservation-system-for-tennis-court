package tennisCourt.MySQL.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name="match_competitions")
public class MatchCompetitions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn
    private Competition competition;
    @OneToOne(cascade =  CascadeType.REMOVE)
    private Matches matches;

    public MatchCompetitions() {
    }

    public MatchCompetitions(Competition competition) {
        this.competition = competition;
    }
}
