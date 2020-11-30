package tennisCourt.MySQL.model;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.concurrent.ThreadLocalRandom;

@Data
@Entity
@Table(name="matches")
public class Matches {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(mappedBy = "matches", cascade = CascadeType.ALL)
    private MatchCompetitions matchCompetitions;
    @OneToOne(mappedBy = "matches", cascade = CascadeType.ALL)
    private PlayersMatch match1;
    @OneToOne
    private Team team1;
    @OneToOne
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
        this.team1.setMatch1(this);
        this.team2.setMatch2(this);
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
        this.team1.setMatch1(this);
        this.team2.setMatch2(this);
    }
}
