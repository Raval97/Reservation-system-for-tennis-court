package tennisCourt.MySQL.model;

import lombok.Data;

import javax.persistence.*;
import java.util.concurrent.ThreadLocalRandom;

@Data
@Entity
@Table(name = "players_match")
public class PlayersMatch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    private PersonalData player;
    @OneToOne
    private Matches matches;
    float deegree;
    int minutes;
    float distance;
    int shots;
    int shots_on_target;
    int goals;
    int passes;
    int accurate_passes;
    int assists;
    int fouls;
    int red_cards;
    int yellow_cards;
    boolean injury;
    int integration_attempts;
    int integrations;
    int defence_attempts;
    int defences;

    public PlayersMatch() {
    }

    public PlayersMatch(PersonalData player, Matches match) {
        this.deegree = ThreadLocalRandom.current().nextInt(1, 6);
        this.minutes = 90;
        this.distance = ThreadLocalRandom.current().nextInt(6, 14);
        this.shots = ThreadLocalRandom.current().nextInt(0, 7);
        this.shots_on_target = ThreadLocalRandom.current().nextInt(0, 3);
        this.goals = ThreadLocalRandom.current().nextInt(0, 3);
        this.passes = ThreadLocalRandom.current().nextInt(10, 40);
        this.accurate_passes = ThreadLocalRandom.current().nextInt(10, 30);
        this.assists = ThreadLocalRandom.current().nextInt(0, 3);
        this.fouls = ThreadLocalRandom.current().nextInt(0, 5);
        this.red_cards = ThreadLocalRandom.current().nextInt(0, 1);
        this.yellow_cards = ThreadLocalRandom.current().nextInt(0, 2);
        this.injury = false;
        this.integration_attempts = ThreadLocalRandom.current().nextInt(0, 10);
        this.integrations = ThreadLocalRandom.current().nextInt(0, 10);
        this.defence_attempts = ThreadLocalRandom.current().nextInt(0, 10);
        this.defences =  ThreadLocalRandom.current().nextInt(00, 10);
    }

    public PlayersMatch(float deegree, int minutes, float distance, int shots, int shots_on_target, int goals,
                        int passes, int accurate_passes, int assists, int fouls, int red_cards, int yellow_cards,
                        boolean injury, int integration_attempts, int integrations, int defence_attempts, int defences,
                        PersonalData player, Matches match) {
        this.deegree = deegree;
        this.minutes = minutes;
        this.distance = distance;
        this.shots = shots;
        this.shots_on_target = shots_on_target;
        this.goals = goals;
        this.passes = passes;
        this.accurate_passes = accurate_passes;
        this.assists = assists;
        this.fouls = fouls;
        this.red_cards = red_cards;
        this.yellow_cards = yellow_cards;
        this.injury = injury;
        this.integration_attempts = integration_attempts;
        this.integrations = integrations;
        this.defence_attempts = defence_attempts;
        this.defences = defences;
        this.player = player;
        this.matches = match;
        this.player.setPlayersMatch(this);
        this.matches.setMatch1(this);
    }
}
