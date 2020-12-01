package tennisCourt.MySQL.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

@Data
@Entity
@Table(name="matches")
public class Matches {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonBackReference
    @OneToOne(mappedBy = "matches", cascade = CascadeType.ALL)
    private MatchCompetitions matchCompetitions;
    @JsonBackReference
    @OneToMany(mappedBy = "matches", cascade = CascadeType.ALL)
    private Set<PlayersMatch> match1;
    @ManyToOne
    @JoinColumn
    private Team team1;
    @ManyToOne
    @JoinColumn
    private Team team2;
    private int team_1_score;
    private int team_2_score;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    public Matches() {
    }

    public Matches(MatchCompetitions matchCompetitions,
                   Team team1, Team team2) {
        this.team_1_score = ThreadLocalRandom.current().nextInt(0, 5);
        this.team_2_score = ThreadLocalRandom.current().nextInt(0, 5);
        this.date = LocalDate.now().minusDays(ThreadLocalRandom.current().nextInt(0, 1000));
        this.matchCompetitions = matchCompetitions;
        this.matchCompetitions.setMatches(this);
        this.team1 = team1;
        this.team2 = team2;
    }

    public Matches(int team_1_score, int team_2_score, LocalDate date, MatchCompetitions matchCompetitions,
                   Team team1, Team team2) {
        this.team_1_score = team_1_score;
        this.team_2_score = team_2_score;
        this.date = date;
        this.matchCompetitions = matchCompetitions;
        this.matchCompetitions.setMatches(this);
        this.team1 = team1;
        this.team2 = team2;
    }
}
