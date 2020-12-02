package tennisCourt.MySQL.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

@Data
@Entity
@Table(name="Competition")
public class Competition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonBackReference
    @OneToMany(mappedBy = "competition", cascade = CascadeType.ALL)
    private Set<MatchCompetitions> matchCompetitions;
    @JsonBackReference
    @OneToMany(mappedBy = "competition", cascade = CascadeType.ALL)
    private Set<CompetitionTeam> competitionTeams;
    String name;
    String level;
    String session;
    int number_of_teams;

    public Competition(String name) {
        this.name = name;
        this.level = "1";
        this.session = ThreadLocalRandom.current().nextInt(0, 3)%2==1 ? "zimowa" : "wiosenna";
        this.number_of_teams = 25;
    }

    public Competition(String name, String level, String session, int number_of_teams) {
        this.name = name;
        this.level = level;
        this.session = session;
        this.number_of_teams = number_of_teams;
    }
}
