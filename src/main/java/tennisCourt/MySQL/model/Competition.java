package tennisCourt.MySQL.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

@Data
@Entity
@Table(name="competition")
public class Competition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany(mappedBy = "competition", cascade = CascadeType.ALL)
    private Set<MatchCompetitions> matchCompetitions;
    String name;
    String level;
    String session;
    int number_of_teams;

    public Competition() {
        this.name = "Ekstraklasa";
        this.level = "1";
        this.session = ThreadLocalRandom.current().nextInt(0, 2)%2==1 ? "zimowa" : "wiosenna";
        this.number_of_teams = 25;
    }

    public Competition(String name, String level, String session, int number_of_teams) {
        this.name = name;
        this.level = level;
        this.session = session;
        this.number_of_teams = number_of_teams;
    }
}
